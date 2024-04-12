package com.example.tmawarehouse.Model;

import com.example.tmawarehouse.Data.DialogData;
import com.example.tmawarehouse.Data.Item;
import com.example.tmawarehouse.Data.TMA_Requests;
import javafx.scene.control.TableView;

import java.sql.PreparedStatement;
import java.sql.Statement;

public interface ItemRepository {


    PreparedStatement insertNewItem(DialogData dialogData, PreparedStatement preparedStatement);
    int selectItemId(DialogData dialogData, Statement statement);
    void updateQuantity(PreparedStatement preparedStatement, TMA_Requests selectedRequest);
    void updateRejectStatus(PreparedStatement preparedStatement, TMA_Requests selectedRequest, String reason);

    void delete(int itemId, PreparedStatement preparedStatement, TableView<Item> itemTableView);
//    List<Item> selectAll();
}
