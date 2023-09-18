/**
 * 
 */
package com.cmm.jft.connector.engine;


import java.util.concurrent.ConcurrentLinkedQueue;

import com.cmm.jft.messaging.MessageRepository;
import com.cmm.jft.messaging.fix44.Fix44EngineMessageEncoder;
import com.cmm.jft.messaging.handlers.RouterMessageHandler;
import com.cmm.jft.model.trading.Orders;

import quickfix.Application;
import quickfix.DataDictionary;
import quickfix.DoNotSend;
import quickfix.FieldNotFound;
import quickfix.IncorrectDataFormat;
import quickfix.IncorrectTagValue;
import quickfix.Message;
import quickfix.MessageCracker;
import quickfix.RejectLogon;
import quickfix.Session;
import quickfix.SessionID;
import quickfix.UnsupportedMessageType;
import quickfix.fix44.AllocationReport;
import quickfix.fix44.BusinessMessageReject;
import quickfix.fix44.ExecutionReport;
import quickfix.fix44.OrderCancelReject;
import quickfix.fix44.PositionMaintenanceReport;
import quickfix.fix44.Quote;
import quickfix.fix44.QuoteCancel;
import quickfix.fix44.QuoteRequest;
import quickfix.fix44.QuoteRequestReject;
import quickfix.fix44.QuoteStatusReport;
import quickfix.fix44.SecurityDefinition;

/**
 * <p><code>EngineConnector.java</code></p>
 * @author Cristiano M Martins
 * @version 30-07-2015 23:54:28
 *
 */
public class EngineConnector extends MessageCracker implements Application, RouterMessageHandler{

    private SessionID sessionID;
    private ConcurrentLinkedQueue<Message> messages;
    private static EngineConnector instance;

    private EngineConnector() {
	this.messages = new ConcurrentLinkedQueue<Message>();
    }


    public synchronized static EngineConnector getInstance() {
	if(instance == null) {
	    instance = new EngineConnector();
	}
	return instance;
    }



    public void newOrderSingle(Orders ordr) {
	Message m = Fix44EngineMessageEncoder.getInstance().newOrderSingle(ordr);
	MessageRepository.getInstance().addMessage(m, sessionID);
    }

    public void cancelReplaceRequest(Orders ordr) {
	Message m = Fix44EngineMessageEncoder.getInstance().orderCancelReplaceRequest(ordr);
	MessageRepository.getInstance().addMessage(m, sessionID);
    }

    public void cancelRequest(Orders ordr) {
	Message m = Fix44EngineMessageEncoder.getInstance().orderCancelRequest(ordr);
	MessageRepository.getInstance().addMessage(m, sessionID);
    }

    public void newOrderCross(Orders ordr) {
	Message m = Fix44EngineMessageEncoder.getInstance().newOrderCross(ordr);
	MessageRepository.getInstance().addMessage(m, sessionID);
    }


    /* (non-Javadoc)
     * @see quickfix.Application#fromAdmin(quickfix.Message, quickfix.SessionID)
     */
    @Override
    public void fromAdmin(Message message, SessionID sessionID)
	    throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, RejectLogon {
	System.out.println("fromAdmin:" + message);
    }


    /* (non-Javadoc)
     * @see quickfix.Application#fromApp(quickfix.Message, quickfix.SessionID)
     */
    @Override
    public void fromApp(Message message, SessionID sessionID)
	    throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {
	//System.out.println("fromApp:" + message);
	crack(message, sessionID);
    }


    /* (non-Javadoc)
     * @see quickfix.Application#onCreate(quickfix.SessionID)
     */
    @Override
    public void onCreate(SessionID sessionID) {
	// TODO Auto-generated method stub
	//System.out.println("onCreate:");
    }


    /* (non-Javadoc)
     * @see quickfix.Application#onLogon(quickfix.SessionID)
     */
    @Override
    public void onLogon(SessionID sessionID) {
	if(Session.doesSessionExist(sessionID)){
	    this.sessionID = sessionID;
	    Session.lookupSession(sessionID).logon();
	}
    }


    /* (non-Javadoc)
     * @see quickfix.Application#onLogout(quickfix.SessionID)
     */
    @Override
    public void onLogout(SessionID sessionID) {
	this.sessionID = null;
    }


    /* (non-Javadoc)
     * @see quickfix.Application#toAdmin(quickfix.Message, quickfix.SessionID)
     */
    @Override
    public void toAdmin(Message message, SessionID sessionID) {
	// TODO Auto-generated method stub

    }


    /* (non-Javadoc)
     * @see quickfix.Application#toApp(quickfix.Message, quickfix.SessionID)
     */
    @Override
    public void toApp(Message message, SessionID sessionID) throws DoNotSend {
	// TODO Auto-generated method stub
	
    }


    /* (non-Javadoc)
     * @see com.cmm.jft.connector.message.ClientEngineMessageHandler#onMessage(quickfix.fix44.BusinessMessageReject, quickfix.SessionID)
     */
    @Override
    public void onMessage(BusinessMessageReject message, SessionID sessionID)
	    throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
	// TODO Auto-generated method stub

    }


    /* (non-Javadoc)
     * @see com.cmm.jft.connector.message.ClientEngineMessageHandler#onMessage(quickfix.fix44.ExecutionReport, quickfix.SessionID)
     */
    @Override
    public void onMessage(ExecutionReport message, SessionID sessionID)
	    throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
	System.out.println("ExecutionReport: " + message);
	//TradingService.getInstance().onExecutionReport(message);
    }


    /* (non-Javadoc)
     * @see com.cmm.jft.connector.message.ClientEngineMessageHandler#onMessage(quickfix.fix44.OrderCancelReject, quickfix.SessionID)
     */
    @Override
    public void onMessage(OrderCancelReject message, SessionID sessionID)
	    throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
	System.out.println(message);
	//TradingService.getInstance().onOrderCancelReject(message);

    }


    /* (non-Javadoc)
     * @see com.cmm.jft.connector.message.ClientEngineMessageHandler#onMessage(quickfix.fix44.SecurityDefinition, quickfix.SessionID)
     */
    @Override
    public void onMessage(SecurityDefinition message, SessionID sessionID)
	    throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
	// TODO Auto-generated method stub

    }


    /* (non-Javadoc)
     * @see com.cmm.jft.connector.message.ClientEngineMessageHandler#onMessage(quickfix.fix44.QuoteRequest, quickfix.SessionID)
     */
    @Override
    public void onMessage(QuoteRequest message, SessionID sessionID)
	    throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
	// TODO Auto-generated method stub

    }


    /* (non-Javadoc)
     * @see com.cmm.jft.connector.message.ClientEngineMessageHandler#onMessage(quickfix.fix44.QuoteStatusReport, quickfix.SessionID)
     */
    @Override
    public void onMessage(QuoteStatusReport message, SessionID sessionID)
	    throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
	// TODO Auto-generated method stub

    }


    /* (non-Javadoc)
     * @see com.cmm.jft.connector.message.ClientEngineMessageHandler#onMessage(quickfix.fix44.Quote, quickfix.SessionID)
     */
    @Override
    public void onMessage(Quote message, SessionID sessionID)
	    throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
	// TODO Auto-generated method stub

    }


    /* (non-Javadoc)
     * @see com.cmm.jft.connector.message.ClientEngineMessageHandler#onMessage(quickfix.fix44.QuoteCancel, quickfix.SessionID)
     */
    @Override
    public void onMessage(QuoteCancel message, SessionID sessionID)
	    throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
	// TODO Auto-generated method stub

    }


    /* (non-Javadoc)
     * @see com.cmm.jft.connector.message.ClientEngineMessageHandler#onMessage(quickfix.fix44.QuoteRequestReject, quickfix.SessionID)
     */
    @Override
    public void onMessage(QuoteRequestReject message, SessionID sessionID)
	    throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
	// TODO Auto-generated method stub

    }


    /* (non-Javadoc)
     * @see com.cmm.jft.connector.message.ClientEngineMessageHandler#onMessage(quickfix.fix44.PositionMaintenanceReport, quickfix.SessionID)
     */
    @Override
    public void onMessage(PositionMaintenanceReport message, SessionID sessionID)
	    throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
	// TODO Auto-generated method stub

    }


    /* (non-Javadoc)
     * @see com.cmm.jft.connector.message.ClientEngineMessageHandler#onMessage(quickfix.fix44.AllocationReport, quickfix.SessionID)
     */
    @Override
    public void onMessage(AllocationReport message, SessionID sessionID)
	    throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
	// TODO Auto-generated method stub

    }


}
