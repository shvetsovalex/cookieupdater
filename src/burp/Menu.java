package burp;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

public class Menu implements IContextMenuFactory {
    private final IExtensionHelpers m_helpers;
    private IBurpExtenderCallbacks m_callbacks;

    public Menu(IExtensionHelpers helpers, IBurpExtenderCallbacks callbacks) {
        m_helpers = helpers;
        m_callbacks = callbacks;
    }

    public List<JMenuItem> createMenuItems(final IContextMenuInvocation invocation) {
        List<JMenuItem> menus = new ArrayList();

        if (invocation.getToolFlag() != IBurpExtenderCallbacks.TOOL_INTRUDER && invocation.getInvocationContext() != IContextMenuInvocation.CONTEXT_MESSAGE_EDITOR_REQUEST){
            return menus;
        }

        JMenuItem changeCookieRepeater = new JMenuItem("Update Cookie");
        changeCookieRepeater.addMouseListener(new MouseListener() {

            public void mouseClicked(MouseEvent arg0) {

            }


            public void mouseEntered(MouseEvent arg0) {
            }


            public void mouseExited(MouseEvent arg0) {
            }


            public void mousePressed(MouseEvent arg0) {

            }


            public void mouseReleased(MouseEvent arg0) {
                IHttpRequestResponse requestResponse = invocation.getSelectedMessages()[0];
                try {
                    IHttpRequestResponse historyItem;
                    String freshCookie = null;
                    byte[] freshRequest = null;
                    for (int i = m_callbacks.getProxyHistory().length - 1; i >=0; i--)
                    {
                        historyItem = m_callbacks.getProxyHistory()[i];
                        if ( historyItem.getHttpService().getHost().equals(requestResponse.getHttpService().getHost())
                                && historyItem.getHttpService().getPort() == requestResponse.getHttpService().getPort() )
                        {
                            if ( ( freshCookie = getCookieHeader( m_helpers.analyzeRequest(historyItem.getRequest()).getHeaders() ) )
                                    != null )
                            {
                                freshRequest = replaceCookie(freshCookie
                                        , invocation.getSelectedMessages()[0].getRequest()).getBytes();
                                invocation.getSelectedMessages()[0].setRequest( freshRequest );
                                break;
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        menus.add(changeCookieRepeater);
        return menus;
    }

    private String getCookieHeader( List<String> headers )
    {
        for ( String header: headers)
        {
            if ( header.indexOf( "Cookie: " ) == 0 )
            {
                return header;
            }
        }
        return null;
    }

    private String replaceCookie( String freshCookie, byte[] request )
    {
        String httpPacket = "";
        boolean replaced = false;
        List<String> headersList = m_helpers.analyzeRequest( request ).getHeaders();
        for ( int i = 0; i < headersList.size(); i++ )
        {
            if ( headersList.get(i).indexOf("Cookie: ") == 0 )
            {
                httpPacket += freshCookie + "\r\n";
                replaced = true;
                for ( int j = i + 1; j < headersList.size(); j++ )
                {
                    httpPacket += headersList.get(j) + "\r\n";
                }
                break;
            } else {
                httpPacket += headersList.get(i) + "\r\n";
            }
        }
        if (!replaced)
        {
            httpPacket += freshCookie + "\r\n";
        }
        httpPacket += "\r\n" +
                new String( request ).substring( m_helpers.analyzeRequest( request ).getBodyOffset() );
        return httpPacket;
    }

}