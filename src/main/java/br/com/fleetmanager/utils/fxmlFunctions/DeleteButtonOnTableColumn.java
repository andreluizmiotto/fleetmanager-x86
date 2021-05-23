package br.com.fleetmanager.utils.fxmlFunctions;

import br.com.fleetmanager.abstracts.ABaseDAO;
import br.com.fleetmanager.abstracts.ABaseModel;
import br.com.fleetmanager.connection.implementation.ConnectionFactory;
import br.com.fleetmanager.utils.Functions;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

import java.sql.Connection;
import java.sql.SQLException;

public class DeleteButtonOnTableColumn<T> {

    public Callback<TableColumn<T, Void>, TableCell<T, Void>> getDeleteButton(ABaseDAO objectDAO) {

        return new Callback<TableColumn<T, Void>, TableCell<T, Void>>() {
            @Override
            public TableCell<T, Void> call(final TableColumn<T, Void> param) {
                return new TableCell<T, Void>() {

                    private final Button btn = new Button("", Functions.getImageView(getClass(), "trash_16px.png"));

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            //TODO Implement confirmation message before execute action
                            try (Connection connection = new ConnectionFactory().getNewConnection()) {
                                T object = getTableView().getItems().get(getIndex());
                                objectDAO.setConnection(connection);
                                objectDAO.Delete(((ABaseModel) object).getId());
                                getTableView().getItems().remove(object);
                            } catch (SQLException throwables) {
                                throwables.printStackTrace();
                            }
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
            }
        };
    }
}
