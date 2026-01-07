import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Close implements ActionListener {
//butonul de close inchide tot
    @Override
    public void actionPerformed(ActionEvent e) {
        System.exit(0);
    }
}
