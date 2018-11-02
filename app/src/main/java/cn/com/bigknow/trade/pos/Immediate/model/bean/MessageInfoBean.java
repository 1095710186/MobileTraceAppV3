package cn.com.bigknow.trade.pos.Immediate.model.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;

import java.io.Serializable;

/**
 * Created by laixy
 * "msgType": 消息类型
 * "receiverName": 接受人姓名
 * "corrId": 关联业务ID
 * "content": 消息内容
 * "downloadTime": null,
 * "sendTime": 发送时间
 * "senderId": 发送人userId
 * "receiverId": 接收人userId
 * "transType": 业务类型 ENTRY_PASS入场通过，ENTRY_JEJECT-入场驳回，ORDER_NEW-新增订单，ORDER_CANCEL-撤销订单
 * "sender": 发送人
 * "msgTitle": 消息标题
 * "id": 消息ID
 * "corrExtId": null,
 * "receiveType": 接收者类型，U-用户，M-商户
 */
@Entity
public class MessageInfoBean implements Serializable{
    static final long serialVersionUID = 42L;
    private String msgType;
    private String receiverName;
    private String corrId;
    private String content;

    private String sendTime;
    private String senderId;
    private String receiverId;
    private String transType;

    private String sender;
    private String msgTitle;
    @Unique
    @Id
    private String id;
    private String receiveType;
    private int type;//0=采购，购物侧等消息，1=审批信息  2=系统消息 3=资金消息  4==追溯公告

    @Generated(hash = 122469485)
    public MessageInfoBean(String msgType, String receiverName, String corrId,
                           String content, String sendTime, String senderId, String receiverId,
                           String transType, String sender, String msgTitle, String id, String receiveType,
                           int type) {
        this.msgType = msgType;
        this.receiverName = receiverName;
        this.corrId = corrId;
        this.content = content;
        this.sendTime = sendTime;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.transType = transType;
        this.sender = sender;
        this.msgTitle = msgTitle;
        this.id = id;
        this.receiveType = receiveType;
        this.type = type;
    }

    @Generated(hash = 247796383)
    public MessageInfoBean() {
    }

    @Override
    public String toString() {
        return "MessageInfoBean{" +
                "msgType='" + msgType + '\'' +
                ", receiverName='" + receiverName + '\'' +
                ", corrId='" + corrId + '\'' +
                ", content='" + content + '\'' +
                ", sendTime='" + sendTime + '\'' +
                ", senderId='" + senderId + '\'' +
                ", receiverId='" + receiverId + '\'' +
                ", transType='" + transType + '\'' +
                ", sender='" + sender + '\'' +
                ", msgTitle='" + msgTitle + '\'' +
                ", id='" + id + '\'' +
                ", receiveType='" + receiveType + '\'' +
                ", type=" + type +
                '}';
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getCorrId() {
        return corrId;
    }

    public void setCorrId(String corrId) {
        this.corrId = corrId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMsgTitle() {
        return msgTitle;
    }

    public void setMsgTitle(String msgTitle) {
        this.msgTitle = msgTitle;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReceiveType() {
        return receiveType;
    }

    public void setReceiveType(String receiveType) {
        this.receiveType = receiveType;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
