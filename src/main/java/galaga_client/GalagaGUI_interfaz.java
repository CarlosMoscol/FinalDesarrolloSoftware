package galaga_client;

import javax.accessibility.Accessible;
import javax.swing.*;
import java.awt.*;
import java.awt.image.ImageObserver;
import java.io.Serializable;

public interface GalagaGUI_interfaz extends ImageObserver, MenuContainer, Serializable, Accessible, WindowConstants, RootPaneContainer {
    void setConnectionPanel();

    //
    void setBoardPanel();

    void setBoardText(String text);

    void setMessageText(String text);

    void homeRoom();

    void waitRoomMaster();

    void waitRoomSlave();

    void startGame();

    void setAllFocusable(boolean flag);
}
