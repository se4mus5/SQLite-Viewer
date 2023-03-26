package viewer;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;

public class ApplicationRunner {
    public static void main(String[] args) throws InterruptedException, InvocationTargetException {
        //new SQLiteViewer();
        Runnable initFrame = SQLiteViewer::new;
        SwingUtilities.invokeAndWait(initFrame);
    }
}
