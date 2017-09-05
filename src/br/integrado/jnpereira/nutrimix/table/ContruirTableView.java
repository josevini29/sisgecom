package br.integrado.jnpereira.nutrimix.table;

import br.integrado.jnpereira.nutrimix.dao.Coluna;
import java.lang.annotation.Annotation;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

public class ContruirTableView<M> extends TableView<M> {

    private static final String cssDefault = "-fx-font-weight: bold;";

    public static <T> TableView<T> Criar(TableView table, Class<T> model) {
        ArrayList<TableColumn<T, ?>> columns = new ArrayList<>();

        Set<String> methodNames = getMethodNames(model.getDeclaredMethods());

        for (Field field : model.getDeclaredFields()) {
            String fieldName = field.getName();
            String getterName = "get" + capitalizeFirst(fieldName);
            String setterName = "set" + capitalizeFirst(fieldName);
            boolean notAnnotated = field.getAnnotation(IgnoreTable.class) == null;
            if (notAnnotated && methodNames.contains(getterName)
                    && methodNames.contains(setterName)) {
                Annotation a = field.getAnnotation(Coluna.class);
                Coluna m = (Coluna) a;
                TableColumn column = new TableColumn(m.nome());
                if (field.getAnnotation(Style.class) != null) {
                    Annotation sty = field.getAnnotation(Style.class);
                    Style style = (Style) sty;
                    column.setStyle(cssDefault + style.css());
                }
                column.setCellValueFactory(getCellFactory(model,
                        field.getType(),
                        fieldName));
                columns.add(column);
            }
        }

        table.getColumns().addAll(columns);

        return table;
    }

    private static <S, T> PropertyValueFactory<S, T> getCellFactory(Class<S> model,
            Class<T> fieldType,
            String fieldName) {
        return new PropertyValueFactory<>(fieldName);
    }

    private static String capitalizeFirst(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    private static Set<String> getMethodNames(Method[] methods) {
        Set<String> membersNames = new TreeSet<String>();
        for (Method member : methods) {
            membersNames.add(member.getName());
        }
        return membersNames;
    }
}
