package com.procesos.colas.application.services.alfresco;


import com.prolinktic.sgdea.domain.model.SeccionSubSeccion.SeccionSubSeccion;
import com.prolinktic.sgdea.domain.model.Serie.Serie;
import com.prolinktic.sgdea.domain.model.alfesco.CreateNode;
import com.prolinktic.sgdea.domain.model.alfesco.NodeType;
import com.prolinktic.sgdea.domain.model.alfesco.NodesResponse;
import com.prolinktic.sgdea.infrastructure.dao.SeccionSubSeccion.SeccionSubSeccionDao;
import com.prolinktic.sgdea.infrastructure.dao.Serie.SerieDao;
import com.prolinktic.sgdea.infrastructure.gateway.AlfrescoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class InstalarCarpetasService {

    private static final Logger LOGGER = LoggerFactory.getLogger(InstalarCarpetasService.class);

    @Autowired
    private SeccionSubSeccionDao seccionSubSeccionDao;

    @Autowired
    private SerieDao serieDao;

    @Autowired
    private AlfrescoClient alfrescoClient;

    private Map<Integer, Object> dependenciesAlfresco;
    private Map<Integer, Object> seriesAlfresco;

    @Async
    public void run(String version, String nameVersion) throws InterruptedException {
        String principalNode = this.createPrincipalFolder(nameVersion);
        dependenciesAlfresco = new HashMap<>();
        seriesAlfresco = new HashMap<>();
        List<SeccionSubSeccion> seccionSubSeccionActualizar = new ArrayList<>();
        List<Serie> seriesActualizar = new ArrayList<>();
        List<SeccionSubSeccion> listadoSecciones = this.obtenerSecciones(version);
        List<Integer> listadoIdSecciones = listadoSecciones.stream().map(seccion -> seccion.getIdSeccionSubSeccion()).collect(Collectors.toList());
        List<Serie> obtenerListadoSeries = this.obtenerSeries(listadoIdSecciones);
        List<SeccionSubSeccion> seccionesPadres = listadoSecciones.stream().filter(seccion -> seccion.getPadre()==null).collect(Collectors.toList());
        for (SeccionSubSeccion seccion : seccionesPadres) {
            this.crearSeccion(seccion, principalNode, seccionSubSeccionActualizar);
        }
        List<SeccionSubSeccion> subSecciones = listadoSecciones.stream().filter(seccion -> seccion.getPadre()!=null).collect(Collectors.toList());
        for (SeccionSubSeccion seccion : subSecciones) {
            this.crearSeccion(seccion, principalNode, seccionSubSeccionActualizar);
        }
        List<Serie> listadoDeSecciones = obtenerListadoSeries.stream().filter(serie -> serie.getPadre()==null).collect(Collectors.toList());
        for (Serie serie : listadoDeSecciones){
            this.createSerie(serie, seriesActualizar);
        }
        List<Serie> listadoDeSubSecciones = obtenerListadoSeries.stream().filter(serie -> serie.getPadre()!=null).collect(Collectors.toList());
        for(Serie serie : listadoDeSubSecciones){
            this.crearSubserie(serie, seriesActualizar);
        }
        LOGGER.info("Creacion de estructura exitosa");
        seccionSubSeccionDao.actualizacionMasiva(seccionSubSeccionActualizar);
        serieDao.actualizacionMasiva(seriesActualizar);

    }

    private String encontrarIdAlfrescoDependencias(Integer id){
        SeccionSubSeccion padre = (SeccionSubSeccion) dependenciesAlfresco.get(id);
        if(padre == null)
            throw new IllegalArgumentException("No se encontro dependencia padre para la dependencias "+id);
        return padre.getIdAlfresco();
    }

    private String encontrarIdAlfrescoSeries(Integer id){
        Serie padre = (Serie) seriesAlfresco.get(id);
        if(padre == null)
            throw new IllegalArgumentException("No se encontro dependencia padre para la dependencias "+id);
        return padre.getIdAlfresco();
    }

    private void createSerie(Serie serie, List<Serie> seriesActualizar){
        CreateNode node = CreateNode.builder()
                .name(serie.getDescripcion())
                .nodeType(NodeType.SERIE.getValue())
                .build();
        Map<String,Object> properties = new HashMap<>();
        properties.put("ltksg:codigo_serie", serie.getCodigo());
        properties.put("ltksg:descripcion_serie", serie.getDescripcion());
        properties.put("ltksg:fechainicial_serie", serie.getFecha_vigencia_inicial().toString());
        properties.put("ltksg:fechafinal_serie", serie.getFecha_vigencia_final().toString());
        properties.put("ltksg:estado_serie", true);
        node.setProperties(properties);
        String idPadre = this.encontrarIdAlfrescoDependencias(serie.getSeccionSubSeccion().getIdSeccionSubSeccion());
        NodesResponse response = alfrescoClient.crearNodo(idPadre, node);
        serie.setIdAlfresco(response.getEntry().getId());
        seriesAlfresco.put(serie.getIdSeriesubserie(), serie);
        seriesActualizar.add(serie);
    }
    private void crearSubserie(Serie serie, List<Serie> seriesActualizar) {
        CreateNode node = CreateNode.builder()
                .name(serie.getDescripcion())
                .nodeType(NodeType.SUBSERIE.getValue())
                .build();
        Map<String,Object> properties = new HashMap<>();
        properties.put("ltksg:codigo_subserie", serie.getCodigo());
        properties.put("ltksg:codigo_subserie_serie", serie.getPadre().getCodigo());
        properties.put("ltksg:tiempo_archivo_gestion_subserie", serie.getTiempoArchivoGestion());
        properties.put("ltksg:tiempo_archivo_central_subserie_subserie", serie.getTiempoArchivoCentral());
        properties.put("ltksg:disposicion_final_subserie", "");
        properties.put("ltksg:estado_subserie", true);
        properties.put("ltksg:procedimiento", serie.getProcedimiento());
        node.setProperties(properties);
        String idPadre = this.encontrarIdAlfrescoSeries(serie.getPadre().getIdSeriesubserie());
        NodesResponse response = alfrescoClient.crearNodo(idPadre, node);
        serie.setIdAlfresco(response.getEntry().getId());
        seriesAlfresco.put(serie.getIdSeriesubserie(), serie);
        seriesActualizar.add(serie);
    }

    private void crearSeccion(SeccionSubSeccion seccion, String principalFolder, List<SeccionSubSeccion> listadoActualiza){
        boolean tienePadre = (seccion.getPadre() == null) ? false : true;
        CreateNode nodo = CreateNode.builder()
                .nodeType(NodeType.DEPENDENCIA.getValue())
                .name(seccion.getNombre())
                .build();
        Map<String,Object> properties = new HashMap<>();
        properties.put("ltksg:codigo_dependencia", seccion.getCodigo());
        properties.put("ltksg:nombre_dependencia", seccion.getNombre());
        String seccion1 = tienePadre ? seccion.getPadre().getNombre(): seccion.getNombre();
        String subSeccion = tienePadre ? seccion.getNombre() : null ;
        properties.put("ltksg:seccion", seccion1);
        properties.put("ltksg:sub_seccion", subSeccion);
        properties.put("ltksg:trd_cargada", true);
        nodo.setProperties(properties);
        String idNodo = tienePadre ? this.encontrarIdAlfrescoDependencias(seccion.getPadre().getIdSeccionSubSeccion()) : principalFolder;
        NodesResponse response = alfrescoClient.crearNodo(idNodo, nodo);
        seccion.setIdAlfresco(response.getEntry().getId());
        dependenciesAlfresco.put(seccion.getIdSeccionSubSeccion(), seccion);
        listadoActualiza.add(seccion);
    }

    private List<SeccionSubSeccion> obtenerSecciones(String version) {
        return seccionSubSeccionDao.obtenerSeccionesPorVersionId(Integer.valueOf(version));
    }
    private List<Serie> obtenerSeries(List<Integer> idSecicones) {
        return serieDao.obtenerSeriesListaDeSecciones(idSecicones);
    }

    private String createPrincipalFolder(String nameVersion){
        CreateNode node = CreateNode.builder()
                .name(nameVersion)
                .nodeType("cm:folder")
                .build();
        NodesResponse response = alfrescoClient.crearNodo("-root-", node);
        return response.getEntry().getId();
    }

}
