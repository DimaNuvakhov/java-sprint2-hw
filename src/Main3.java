import managers.FileBackedManager;
import imanagers.Manager;
import mylibrary.MyLibrary;

import java.io.File;

public class Main3 {
    public static void main(String[] args) {
        // Создание файла
        File file = new File("Data.csv");
        // Создание менеджера
        Manager fileBackedManager = FileBackedManager.loadFromFile(file);
        // Просмотр всех задач
        MyLibrary.showAllItems(fileBackedManager);
        // Просмотр истории
        System.out.println(MyLibrary.printHistory(fileBackedManager));
    }
}
