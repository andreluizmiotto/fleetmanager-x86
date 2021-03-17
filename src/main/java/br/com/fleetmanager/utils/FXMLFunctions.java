package br.com.fleetmanager.utils;

import br.com.fleetmanager.abstracts.ABaseDAO;
import br.com.fleetmanager.abstracts.ABaseModel;
import br.com.fleetmanager.connection.ConnectionFactory;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

import java.sql.Connection;
import java.sql.SQLException;

public class FXMLFunctions<T> {

    public Callback<TableColumn<T, Void>, TableCell<T, Void>> getDeleteButton(ABaseDAO objectDAO) {

        Callback<TableColumn<T, Void>, TableCell<T, Void>> cellFactory = new Callback<TableColumn<T, Void>, TableCell<T, Void>>() {
            @Override
            public TableCell<T, Void> call(final TableColumn<T, Void> param) {
                final TableCell<T, Void> cell = new TableCell<T, Void>() {

                    final Node node = new ImageView("file:src/main/resources/icons/trash_16px.png");
                    private final Button btn = new Button("", node);

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            //TODO Implement confirmation message before execute action
                            try(Connection connection = new ConnectionFactory().getNewConnection()) {
                                T object = getTableView().getItems().get(getIndex());
                                objectDAO.setConnection(connection);
                                objectDAO.Delete(((ABaseModel)object).getId());
                                getTableView().getItems().remove(getTableView().getSelectionModel().getSelectedItem());
                            } catch (SQLException throwables) {
                                throwables.printStackTrace();
                            };
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty)
                            setGraphic(null);
                        else
                            setGraphic(btn);
                    }
                };
                return cell;
            }
        };
        return cellFactory;
    }
}
