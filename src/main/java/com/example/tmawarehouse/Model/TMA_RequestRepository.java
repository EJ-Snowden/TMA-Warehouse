package com.example.tmawarehouse.Model;

import com.example.tmawarehouse.Data.Item;

import java.sql.PreparedStatement;

public interface TMA_RequestRepository {
    PreparedStatement insertNewRequest(PreparedStatement preparedStatement, Item selectedItem, int id, String unit,
                          int quantity, double price, String comment);
    void execStatement(PreparedStatement preparedStatement);
}
