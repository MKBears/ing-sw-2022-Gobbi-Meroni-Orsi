package it.polimi.ingsw.client;

public class GuiThread extends Thread{

    private static Gui gui;
    @Override
    public void run() {

    }
    public GuiThread(Gui gui){
        GuiThread.gui=gui;
    }

    public void sync(){
        try {
            gui.wait();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void  noti(){
        gui.notifyAll();
    }


}
