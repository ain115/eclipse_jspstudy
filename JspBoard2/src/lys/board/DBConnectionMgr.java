/**
 * Copyright(c) 2001 iSavvix Corporation (http://www.isavvix.com/)
 *
 *                        All rights reserved
 *
 * Permission to use, copy, modify and distribute this material for
 * any purpose and without fee is hereby granted, provided that the
 * above copyright notice and this permission notice appear in all
 * copies, and that the name of iSavvix Corporation not be used in
 * advertising or publicity pertaining to this material without the
 * specific, prior written permission of an authorized representative of
 * iSavvix Corporation.
 *
 * ISAVVIX CORPORATION MAKES NO REPRESENTATIONS AND EXTENDS NO WARRANTIES,
 * EXPRESS OR IMPLIED, WITH RESPECT TO THE SOFTWARE, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR ANY PARTICULAR PURPOSE, AND THE WARRANTY AGAINST
 * INFRINGEMENT OF PATENTS OR OTHER INTELLECTUAL PROPERTY RIGHTS.  THE
 * SOFTWARE IS PROVIDED "AS IS", AND IN NO EVENT SHALL ISAVVIX CORPORATION OR
 * ANY OF ITS AFFILIATES BE LIABLE FOR ANY DAMAGES, INCLUDING ANY
 * LOST PROFITS OR OTHER INCIDENTAL OR CONSEQUENTIAL DAMAGES RELATING
 * TO THE SOFTWARE.
 *
 */


package lys.board;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.Vector;


/**
 * Manages a java.sql.Connection pool.
 *
 * @author  Anil Hemrajani
 */
public class DBConnectionMgr {

    //而ㅻ꽖�뀡���쓣 踰≫꽣濡� 援ъ꽦
	//private MemberDBMgr mem =null;
    private Vector connections = new Vector(10);
    /*   MySQL */
    //�썝寃�,濡쒖뺄吏��썝�떎 媛��뒫
    //(1) 硫ㅻ쾭蹂��닔 �꽑�뼵
    private String _driver,_url,_user,_password;
    /*
	private String _driver = "org.gjt.mm.mysql.Driver",
    _url = "jdbc:mysql://localhost:3306/mydb2?useUnicode=true&characterEncoding=UTF-8",
    _user = "root",
    _password = "1234";
    */
    /*
    private String _driver = "oracle.jdbc.driver.OracleDriver",
    _url = "jdbc:oracle:thin:@localhost:1521:orcl",
    _user = "scott",
    _password = "tiger";
   */
    private boolean _traceOn = false;
    private boolean initialized = false;
	
	//而ㅻ꽖�뀡�쓣 10媛� 以�鍮�
    private int _openConnections = 10;

    //而ㅻ꽖�뀡��媛앹껜瑜� �꽑�뼵
    private static DBConnectionMgr instance = null;

    //(2)mysql.properties�뙆�씪�쓣 �씫�뼱�뱾�뿬�꽌 �궎媛믪뿉 �빐�떦�릺�뒗 value媛� �뼸�뼱�삤湲�
    public DBConnectionMgr() throws IOException {
    	Properties props=new Properties();
    	FileInputStream in=new FileInputStream
    	("C:/webtest/4.jsp/sou/JspBoard2/WebContent/dbtest/mysql.properties");
    	props.load(in);//�뙆�씪�쓽 �궡�슜 硫붾え由ъ뿉 遺덈윭�삤湲�
    	in.close();
    	
    	_driver=props.getProperty("jdbc.drivers");
    	//�뱶�씪�씠釉뚮쭔 �떆�뒪�뀥�뿉 諛섏쁺
    	if(_driver!=null) System.setProperty("jdbc.drivers", _driver);
    	//-------------------�굹癒몄� �빆紐⑹� 硫ㅻ쾭蹂��닔�뿉 遺덈윭���꽌 ���옣留� �븯硫� �맂�떎.----
    	_url=props.getProperty("jdbc.url");
    	_user=props.getProperty("jdbc.username");
    	_password=props.getProperty("jdbc.password");
    	System.out.println("_driver=>"+(_driver)+",_url="+(_url));
    	System.out.println("_user=>"+(_user)+",_password="+(_password));
    }

    /** Use this method to set the maximum number of open connections before
     unused connections are closed.
     */
  
    //(3)而ㅻ꽖�뀡���쓣 �뼸�뼱�삤�뒗 �젙�쟻硫붿냼�뱶=>�삁�쇅泥섎━�빐�빞 �븳�떎.
    public static DBConnectionMgr getInstance() throws Exception {
        //而ㅻ꽖�뀡���씠 �깮�꽦�씠 �븞�릺�뼱�엳�떎硫�
		if (instance == null) {
            synchronized (DBConnectionMgr.class) {
                //�깮�꽦�씠 �븞�릺�뼱�엳�떎硫�
				if (instance == null) {
					//媛앹껜�깮�꽦
                    instance = new DBConnectionMgr();
                }
            }
        }
        return instance;//�샇異쒗븳 �겢�옒�뒪履쎌쑝濡� 諛섑솚
    }

    public void setOpenConnectionCount(int count) {
        _openConnections = count;
    }


    public void setEnableTrace(boolean enable) {
        _traceOn = enable;
    }


    /** Returns a Vector of java.sql.Connection objects */
    public Vector getConnectionList() {
        return connections;
    }


    /** Opens specified "count" of connections and adds them to the existing pool */
    //珥덇린�뿉 �뿰寃곌컼泥대�� �꽕�젙�빐二쇰뒗 硫붿냼�뱶

	public synchronized void setInitOpenConnections(int count)
            throws SQLException {

        Connection c = null;//�깮�꽦�븷 媛앹껜
        ConnectionObject co = null;//�깮�꽦�븳 �뿰寃곌컼泥�
		                           //愿�由ы빐二쇰뒗 媛앹껜

        for (int i = 0; i < count; i++) {
			//count媛��닔留뚰겮 �뿰寃곌컼泥대�� �깮�꽦
            c = createConnection();
			//踰≫꽣�뿉 �벑濡앺븷 �뿰寃곌컼泥�,���뿬�쑀臾�
            co = new ConnectionObject(c, false);
             //理쒖쥌�쟻�쑝濡� 踰≫꽣�뿉 �뿰寃곌컼泥대�� 異붽�
            connections.addElement(co);
            trace("ConnectionPoolManager: Adding new DB connection to pool (" + connections.size() + ")");
        }
    }


    /** Returns a count of open connections */
    public int getConnectionCount() {
        return connections.size();
    }


    /** Returns an unused existing or new connection.  */
    //�뿰寃곌컼泥대�� �뼸�뼱�삤�뒗 硫붿냼�뱶

	public synchronized Connection getConnection()
            throws Exception {
        if (!initialized) {
			//�젒�냽�븷 DB�쓽 �뱶�씪�씠踰꾨�� 硫붾え由ъ뿉 濡쒕뱶
            Class c = Class.forName(_driver);
			//�옄�룞 �벑濡�(�뱶�씪�씠踰꾪겢�옒�뒪)
            DriverManager.registerDriver((Driver) c.newInstance());

            initialized = true;//�젒�냽�긽�깭 
        }


        Connection c = null;
        ConnectionObject co = null;
		//�궗�슜以묒씠吏� �븡�� �뿰寃곌컼泥�
        boolean badConnection = false;


        for (int i = 0; i < connections.size(); i++) {
			//踰≫꽣�뿉 �뱾�뼱媛� �뿰寃곌컼泥대�� 爰쇰궡�삩�떎.
            co = (ConnectionObject) connections.elementAt(i);

            // If connection is not in use, test to ensure it's still valid!
            if (!co.inUse) {//鍮뚮젮二쇱� �븡�� �긽�깭�씪硫� 
                try {
					//�벐吏��븡�� �긽�깭�쓽 �뿰寃곌컼泥� �뼸�뼱�샂
                    badConnection = co.connection.isClosed();
                    if (!badConnection)
                        badConnection = (co.connection.getWarnings() != null);
                } catch (Exception e) {
                    badConnection = true;
                    e.printStackTrace();
                }

                // Connection is bad, remove from pool
                if (badConnection) { //�벐吏��븡怨� �엳�쑝硫�
				    //踰≫꽣�뿉�꽌 �젣嫄고븯�씪
                    connections.removeElementAt(i);
                    trace("ConnectionPoolManager: Remove disconnected DB connection #" + i);
                    continue;
                }

                c = co.connection;
                co.inUse = true;//鍮뚮젮以� �긽�깭

                trace("ConnectionPoolManager: Using existing DB connection #" + (i + 1));
                break;
            }
        }
        //�슂泥��씠 �뱾�뼱�삩�떎硫�(遺�議깊븯�떎硫�)
        if (c == null) {
            c = createConnection();
            co = new ConnectionObject(c, true);
            connections.addElement(co);//踰≫꽣�뿉異붽�

            trace("ConnectionPoolManager: Creating new DB connection #" + connections.size());
        }

        return c;//諛섑솚
    }


    /** Marks a flag in the ConnectionObject to indicate this connection is no longer in use */
    //諛섎궔�빐二쇰뒗 硫붿냼�뱶
	public synchronized void freeConnection(Connection c) {
        if (c == null)//諛섎궔�빐二쇰뒗 �뿰寃곌컼泥닿� �뾾�쑝硫�
            return;

        ConnectionObject co = null;

        for (int i = 0; i < connections.size(); i++) {
            co = (ConnectionObject) connections.elementAt(i);
            //諛섎궔�븳 �뿰寃곌컼泥�==硫붾え由ъ긽�쓽 李얠� 媛앹껜
			if (c == co.connection) {
                co.inUse = false;//諛섎궔泥섎━
                break;
            }
        }

        for (int i = 0; i < connections.size(); i++) {
            co = (ConnectionObject) connections.elementAt(i);
            //10�씠�긽�쓣 �꽆嫄곕굹 
			//��湲곗쨷�씤 �긽�깭�쓽 �뿰寃곌컼泥닿� �엳�쑝硫�
			if ((i + 1) > _openConnections && !co.inUse)
                removeConnection(co.connection);
        }
    }
    //DB�뿰寃곗쓣 �빐�젣�빐二쇰뒗 硫붿꽌�뱶=>MyBatis
    public void freeConnection(Connection c, PreparedStatement p, ResultSet r) {
        try {
            if (r != null) r.close();
            if (p != null) p.close();
            freeConnection(c);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
   //硫붿꽌�뱶 �삤踰꾨줈�뵫
    public void freeConnection(Connection c, Statement s, ResultSet r) {
        try {
            if (r != null) r.close();
            if (s != null) s.close();
            freeConnection(c);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void freeConnection(Connection c, PreparedStatement p) {
        try {
            if (p != null) p.close();
            freeConnection(c);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void freeConnection(Connection c, Statement s) {
        try {
            if (s != null) s.close();
            freeConnection(c);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /** Marks a flag in the ConnectionObject to indicate this connection is no longer in use */
    public synchronized void removeConnection(Connection c) {
        if (c == null)
            return;

        ConnectionObject co = null;
        for (int i = 0; i < connections.size(); i++) {
            co = (ConnectionObject) connections.elementAt(i);
            if (c == co.connection) {
                try {
                    c.close();
                    connections.removeElementAt(i);
                    trace("Removed " + c.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            }
        }
    }


    private Connection createConnection()
            throws SQLException {
        Connection con = null;

        try {
            if (_user == null)
                _user = "";
            if (_password == null)
                _password = "";

            Properties props = new Properties();
            props.put("user", _user);
            props.put("password", _password);

            con = DriverManager.getConnection(_url, props);
        } catch (Throwable t) {
            throw new SQLException(t.getMessage());
        }

        return con;
    }


    /** Closes all connections and clears out the connection pool */
    public void releaseFreeConnections() {
        trace("ConnectionPoolManager.releaseFreeConnections()");

        Connection c = null;
        ConnectionObject co = null;

        for (int i = 0; i < connections.size(); i++) {
            co = (ConnectionObject) connections.elementAt(i);
            if (!co.inUse)
                removeConnection(co.connection);
        }
    }


    /** Closes all connections and clears out the connection pool */
    public void finalize() {
        trace("ConnectionPoolManager.finalize()");

        Connection c = null;
        ConnectionObject co = null;

        for (int i = 0; i < connections.size(); i++) {
            co = (ConnectionObject) connections.elementAt(i);
            try {
                co.connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            co = null;
        }

        connections.removeAllElements();
    }


    private void trace(String s) {
        if (_traceOn)
            System.err.println(s);
    }

}

//inner class濡� �꽑�뼵
class ConnectionObject {
	//�깮�꽦�맂 �뿰寃곌컼泥�
    public java.sql.Connection connection = null;
    public boolean inUse = false;//���뿬�쑀臾�

    public ConnectionObject(Connection c, boolean useFlag) {
        connection = c;
        inUse = useFlag;
    }
}
