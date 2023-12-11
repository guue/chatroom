package chatRoom;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

public class Msg2Panel {
    /**
     * @MethodName insertMessage
     * @Params  * @param null
     * @Description 更新文本域信息格式化工具
     * @Return
     */
    public static void insertMessage( JTextPane textPane,String title, String content, boolean isSys) {
        StyledDocument document = textPane.getStyledDocument();     /*获取textpane中的文本*/
        /*设置标题的属性*/
        Color content_color = null;
        if (isSys) {
            content_color = Color.RED;
        } else {
            content_color = Color.GRAY;
        }
        SimpleAttributeSet title_attr = new SimpleAttributeSet();
        StyleConstants.setBold(title_attr, true);
        StyleConstants.setForeground(title_attr, Color.BLUE);
        /*设置正文的属性*/
        SimpleAttributeSet content_attr = new SimpleAttributeSet();
        StyleConstants.setBold(content_attr, false);
        StyleConstants.setForeground(content_attr, content_color);
        Style style = null;

        try {
            document.insertString(document.getLength(), title + "\n", title_attr);
            document.insertString(document.getLength(), content + "\n", content_attr);

        } catch (BadLocationException ex) {
            System.out.println("Bad location exception");
        }
        /*设置滑动条到最后*/
        textPane.setCaretPosition(textPane.getDocument().getLength());
//        vertical.setValue(vertical.getMaximum());
    }
}
