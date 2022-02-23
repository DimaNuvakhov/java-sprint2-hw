import inmemorymanagers.FileBackedManager;
import managers.Manager;
import mylibrary.MyLibrary;

import java.io.File;

public class Main3 {
    public static void main(String[] args) {
        // Создаю файл
        File file = new File("Data.csv");
        // Создаю менеджера
        Manager fileBackedManager = FileBackedManager.loadFromFile(file);
        // Просмотр всех задач
        MyLibrary.showAllItems(fileBackedManager);
        // Просмотр истории
        System.out.println(MyLibrary.printHistory(fileBackedManager));
    }
}
