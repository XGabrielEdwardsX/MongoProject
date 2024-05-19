package com.ProyectoMongo.api.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class StockModel {
    private String talla;
    private String color;
    private Integer cantidad;
}