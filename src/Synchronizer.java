import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class Synchronizer implements PropertyChangeListener {
    @Override
    public void propertyChange(PropertyChangeEvent evt){
        if(evt == null)
            return;

        if(evt.getPropertyName().equals("chess")){
            System.out.println("chess");
        }
        if(evt.getPropertyName().equals("numbers")){
            System.out.println("numbers");
        }
    }
}
