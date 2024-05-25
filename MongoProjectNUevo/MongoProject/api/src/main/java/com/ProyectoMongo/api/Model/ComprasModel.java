package com.ProyectoMongo.api.Model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ComprasModel representa una compra en el sistema.
 * Utiliza las anotaciones de Lombok para generar automáticamente
 * los métodos getters, setters, el constructor sin argumentos y el
 * constructor con todos los argumentos.
 * También está mapeada a la colección "compra" en MongoDB.
 * 
 * @see lombok.Data
 * @see lombok.NoArgsConstructor
 * @see lombok.AllArgsConstructor
 * @see org.springframework.data.mongodb.core.mapping.Document
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "compra")
public class ComprasModel {

    /**
     * El identificador único de la compra, generado por MongoDB.
     */
    @Id
    private ObjectId id = new ObjectId();

    /**
     * El identificador del usuario que realizó la compra.
     */
    private ObjectId idUsuario;

    /**
     * La lista de detalles de la compra.
     * 
     * @see DetallesCompraModel
     */
    private List<DetallesCompraModel> detallesCompra;

    /**
     * La tarjeta utilizada para la compra.
     * 
     * @see TarjetasModel
     */
    private TarjetasModel tarjeta;

    /**
     * El estado de la compra (e.g., "pendiente", "completada", "cancelada").
     */
    private String estado;

    /**
     * La descripción de la compra.
     */
    private String descripcion;

    /**
     * La fecha en la que se realizó la compra.
     */
    private Date fechaCompra;

    /**
     * El destinatario de la compra.
     * 
     * @see DestinatariosModel
     */
    private DestinatariosModel destinatario;

    /**
     * Indica si la compra está activa.
     */
    private boolean compraActiva;

    /**
     * El precio total de la compra.
     */
    private BigDecimal precioTotal;
}
