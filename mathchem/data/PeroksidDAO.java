package mathchem.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class PeroksidDAO extends DataAccessObject {

	private ExpItem readExp(ResultSet rs) throws SQLException {
		Long idExp = new Long(rs.getLong("idexp"));
		String time = rs.getString("timez");
		String concentration = rs.getString("conctr");

		ExpItem expItem = new ExpItem();
		expItem.setIdExp(idExp);
		expItem.setTime(time);
		expItem.setConcentration(concentration);
				
		return expItem;
	}
	
	private CatalystItem readCatalyst(ResultSet rs) throws SQLException {		
		
		Long id = new Long(rs.getLong("idcalc"));
		String name = rs.getString("catalyst");
		String method = rs.getString("params");
		String ea = rs.getString("ea");
		String lnk0 = rs.getString("lnk0");

		CatalystItem catalystItem = new CatalystItem();
		catalystItem.setId(id);
		catalystItem.setName(name);
		catalystItem.setMethod(method);
		catalystItem.setEa(ea);
		catalystItem.setLnk0(lnk0);		
				
		return catalystItem;
	}

	private ConstItem readConst(ResultSet rs) throws SQLException {
		
		Long idCalc = new Long(rs.getLong("idcalc"));
		Long idExp = new Long(rs.getLong("idexp"));
		String constant = rs.getString("direct");
		String c0 = rs.getString("c0");
		String tempr = rs.getString("tempr");

		ConstItem constItem = new ConstItem();
		
		constItem.setIdCalc(idCalc);
		constItem.setIdExp(idExp);		
		constItem.setConstant(constant);
		constItem.setC0(c0);
		constItem.setTempr(tempr);
				
		return constItem;
	}
	
	public List<ExpItem> getExpData() {
		LinkedList<ExpItem> expItems = new LinkedList<ExpItem>();
		ResultSet rs = null;
		PreparedStatement statement = null;
		Connection connection = null;
		try {
			connection = getConnection();
			String sql = 
					"select idexp,timez,conctr " +
					"from chemtable.expdate " +
					"where idmtrlreac=31 " +
					"order by idexp,timez;"; 
										
			statement = connection.prepareStatement(sql);
			rs = statement.executeQuery();
			while (rs.next()) {
				ExpItem expItem = readExp(rs);
				expItems.add(expItem);
				
			}
			return expItems;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			close(rs, statement, connection);
		}
	}
	
	public List<CatalystItem> getCatalysts() {
		LinkedList<CatalystItem> catalystItems = new LinkedList<CatalystItem>();
		ResultSet rs = null;
		PreparedStatement statement = null;
		Connection connection = null;
		try {
			connection = getConnection();
			String sql = 
					"SELECT c.idcalc,c.params,c.note catalyst, eadirect*0.001 as ea, lna0direct as lnk0 " +
					"FROM chemtable.energyactvn ea, chemtable.calculation c " +
					"where ea.idcalc=c.idcalc and c.idchem=5;"; 
										
			statement = connection.prepareStatement(sql);
			rs = statement.executeQuery();
			while (rs.next()) {
				CatalystItem catalystItem = readCatalyst(rs);
				catalystItems.add(catalystItem);
				
			}
			return catalystItems;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			close(rs, statement, connection);
		}
	}
	
	public List<ConstItem> getConstants() {
		LinkedList<ConstItem> constItems = new LinkedList<ConstItem>();
		ResultSet rs = null;
		PreparedStatement statement = null;
		Connection connection = null;
		try {
			connection = getConnection();
			String sql = 
					"SELECT idcalc, kc.idexp, direct, kc.note as c0, e.tempr " +
					"FROM chemtable.kinconstant kc, " +
					"chemtable.experiment e " +
					"where e.idexp=kc.idexp;"; 
										
			statement = connection.prepareStatement(sql);
			rs = statement.executeQuery();
			while (rs.next()) {
				ConstItem constItem = readConst(rs);
				constItems.add(constItem);
				
			}
			return constItems;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			close(rs, statement, connection);
		}
	}
}
