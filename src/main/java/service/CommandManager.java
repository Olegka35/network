package service;

import client.message.MES_TYPE;
import client.message.Message;
import org.bouncycastle.asn1.x509.IetfAttrSyntax;
import service.elements.IElement;
import service.elements.nic.MyNIC;
import service.elements.nic.NIC;
import service.elements.router.MyRouter;
import service.elements.router.Router;
import service.elements.switches.MySwitch;
import service.elements.switches.Switch;
import service.ip.IP;
import service.lan.LAN;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static client.message.MES_TYPE.ERROR;

public class CommandManager {
    private Message command;
    Map<String, Object> response;

    public CommandManager(Message command) {
        this.command = command;
    }

    Map<String, Object> produce(LAN lan) {
        MES_TYPE type = command.getType();
        Message message = new Message();
        if(type == null) {
            return getErrorResponse("Incorrect command");
        }
        message.setType(type);

        switch (type) {
            case GET_LAN:
                message.setData(lan);
                return getResponse(message);

            case GET_ELEMENT_INFO:
                String param = command.getData().toString();
                IElement element = lan.findElement(param);
                if(element == null)
                    return getErrorResponse("Element was not found");
                message.setData(element);
                return getResponse(message);

            case CONFIGURE_ELEMENT:
                String[] params = (String[])command.getData();
                element = lan.findElement(params[1]);
                IP newIP;
                Integer mask;
                if(element == null) {
                    return getErrorResponse("Element not found");
                }
                if(element instanceof Switch) {
                    return getErrorResponse("Switch cannot been configured");
                }
                if(element instanceof NIC) {
                    try {
                        newIP = new IP(params[2]);
                    } catch(Exception e) {
                        return getErrorResponse("Incorrect IP address");
                    }
                    mask = Integer.parseInt(params[3]);
                    if(mask <= 0 || mask > 32) {
                        return getErrorResponse("Incorrect mask");
                    }
                    Boolean result = ((NIC) element).configureNIC(newIP, mask);
                    if(!result) return getErrorResponse("Error while configuring NIC");

                    message.setData(element);
                    return getResponse(message, true);
                }
                else if(element instanceof Router) {
                    int port = Integer.parseInt(params[2]);
                    if(port < 0 || port >= element.getPorts().size()) {
                        return getErrorResponse("Incorrect port number");
                    }
                    try {
                        newIP = new IP(params[3]);
                    } catch(Exception e) {
                        return getErrorResponse("Incorrect IP address");
                    }
                    mask = Integer.parseInt(params[4]);
                    if(mask <= 0 || mask > 32) {
                        return getErrorResponse("Incorrect mask");
                    }
                    Boolean result = ((Router) element).configurePort(port, newIP, mask);
                    if(!result) {
                        return getErrorResponse("Error while configuring router");
                    }
                    message.setData(element);
                    return getResponse(message, true);
                }

            case REMOVE_ELEMENT:
                IElement element1 = lan.findElement(command.getData().toString());
                Boolean result = lan.removeElement(command.getData().toString());
                if(!result)
                    return getErrorResponse("Unable to remove this element");
                message.setData(element1);
                return getResponse(message, true);

            case CONNECT_ELEMENTS:
                params = (String[])command.getData();
                IElement e1 = lan.findElement(params[1]);
                IElement e2 = lan.findElement(params[2]);
                if(e1 == null || e2 == null)
                    return getErrorResponse("Incorrect elements to connect");
                if(!lan.connectTwoElements(e1, e2))
                    return getErrorResponse("Unable to connect these elements");
                List<IElement> elementList = new ArrayList<>();
                elementList.add(e1); elementList.add(e2);
                message.setData(elementList);
                return getResponse(message, true);

            case DISCONNECT_ELEMENTS:
                params = (String[])command.getData();
                e1 = lan.findElement(params[1]);
                e2 = lan.findElement(params[2]);
                if(e1 == null || e2 == null)
                    return getErrorResponse("Incorrect elements to connect");
                if(!lan.disconnectElements(e1, e2))
                    return getErrorResponse("Unable to disconnect these elements");
                elementList = new ArrayList<>();
                elementList.add(e1); elementList.add(e2);
                message.setData(elementList);
                return getResponse(message, true);

            case PING:
                params = (String[])command.getData();
                e1 = lan.findElement(params[1]);
                e2 = lan.findElement(params[2]);
                if(e1 == null || e2 == null)
                    return getErrorResponse("Unable to ping");
                List<IElement> elements = lan.pingElements(e1, e2);
                message.setData(elements);
                return getResponse(message);

            default:  //CREATE_ROUTER, CREATE_NIC, CREATE_SWITCH
                params = (String[])command.getData();
                if(lan.findElement(params[1]) != null) {
                    return getErrorResponse("Element with this name is already exist");
                }
                if(type == MES_TYPE.CREATE_ROUTER) {
                    Router router = new MyRouter(Integer.parseInt(params[2]), params[1]);
                    lan.addElement(router);
                    message.setData(router);
                    return getResponse(message, true);
                } else if(type == MES_TYPE.CREATE_NIC) {
                    NIC nic = new MyNIC(params[1]);
                    lan.addElement(nic);
                    message.setData(nic);
                    return getResponse(message, true);
                } else if(type == MES_TYPE.CREATE_SWITCH) {
                    Switch aSwitch = new MySwitch(Integer.parseInt(params[2]), params[1]);
                    lan.addElement(aSwitch);
                    message.setData(aSwitch);
                    return getResponse(message, true);
                }
        }

        return getErrorResponse("Incorrect command");
    }

    Map<String, Object> getResponse(Message message) {
        Map<String, Object> map = new HashMap<>();
        map.put("message", message);
        return map;
    }

    Map<String, Object> getResponse(Message message, Boolean broadcast) {
        Map<String, Object> map = new HashMap<>();
        map.put("message", message);
        map.put("broadcast", true);
        return map;
    }

    Map<String, Object> getErrorResponse(String error) {
        Map<String, Object> map = new HashMap<>();
        Message message = new Message();
        message.setType(ERROR);
        message.setData(error);
        map.put("message", message);
        return map;
    }
}
