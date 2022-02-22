import javax.swing.*;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

public class Reader {
   public static String getSourceCode() throws IOException {
       String text = "";
       JFileChooser srcChooser = new JFileChooser("~");
       srcChooser.setDialogTitle("Choose file to compile");
       if (srcChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION ) {
           String filename = srcChooser.getSelectedFile().getPath();
           Scanner scanner= new Scanner(new BufferedInputStream (new FileInputStream(filename)));
           while ( scanner.hasNextLine())
               text+=scanner.nextLine() + "\n";
       }
       return text;
   }
}
