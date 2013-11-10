package mathchem.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class ReactionItemDAO extends DataAccessObject {

	private ReactionItem readReaction(ResultSet rs) throws SQLException {
		Long idReac = new Long(rs.getLong("idReac"));
		Long idGroup = new Long(rs.getLong("idGroup"));
		String nameReac = rs.getString("nameReac");
		String nameGroup = rs.getString("nameGroup");
		String note = rs.getString("note");
	
		ReactionItem reactionItem = new ReactionItem();
		reactionItem.setIdReac(idReac);
		reactionItem.setIdGroup(idGroup);
		reactionItem.setNameReac(nameReac);
		reactionItem.setNameGroup(nameGroup);
		reactionItem.setNote(note);
		return reactionItem;
	}

	public List<ReactionItem> findAll() {
		LinkedList<ReactionItem> reactionItems = new LinkedList<ReactionItem>();
		ResultSet rs = null;
		PreparedStatement statement = null;
		Connection connection = null;
		try {
			connection = getConnection();
			String sql = "select idreac,r.idgroup,namereac,r.note,namegroup " +
					"from chemtable.reaction r,chemtable.rgroup rg " +
					"where r.idgroup=rg.idgroup"; 
										
			//		+ "rgroup.namegroup,reaction.note "
			//		+ "from chemtable.reaction natural join chemtable.rgroup";
			statement = connection.prepareStatement(sql);
			rs = statement.executeQuery();
			while (rs.next()) {
				ReactionItem reactionItem = readReaction(rs);
				reactionItems.add(reactionItem);
				
			}
			return reactionItems;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			close(rs, statement, connection);
		}
	}
	
	private MaterialItem readMaterial(ResultSet rs) throws SQLException {
		Long idMtrl = new Long(rs.getLong("idmtrl"));
		String nameMtrl = rs.getString("namemtrl");
		String chemF = rs.getString("chemf");
		String noteOfMtrl = rs.getString("noteOfMtrl");
		boolean observ = rs.getBoolean("observ");
		boolean interm = rs.getBoolean("interm");
		boolean source = rs.getBoolean("source");
		boolean product = rs.getBoolean("product");
		String noteOfReaction = rs.getString("noteOfReaction");
		
		MaterialItem materialItem = new MaterialItem();
		materialItem.setIdMtrl(idMtrl);
		materialItem.setNameMtrl(nameMtrl);
		materialItem.setChemF(chemF);
		materialItem.setNoteOfMtrl(noteOfMtrl);
		materialItem.setObserv(observ);
		materialItem.setInterm(interm);
		materialItem.setSource(source);
		materialItem.setProduct(product);
		materialItem.setNoteOfReaction(noteOfReaction);
		
		return materialItem;
	}
	
	public List<MaterialItem> findMaterials() {
		LinkedList<MaterialItem> materialItems = new LinkedList<MaterialItem>();
		ResultSet rs = null;
		PreparedStatement statement = null;
		Connection connection = null;
		try {
			connection = getConnection();
			String sql = "SELECT mor.idmtrl,m.namemtrl,chemf,m.note as noteOfMtrl, " +
					"observ, interm, source, product, mor.note as noteOfReaction " +
					"FROM chemtable.materialofreac mor,chemtable.material m " +
					"where mor.idmtrl=m.idmtrl";

			statement = connection.prepareStatement(sql);
			rs = statement.executeQuery();
			while (rs.next()) {
				MaterialItem materialItem = readMaterial(rs);
				materialItems.add(materialItem);
				
			}
			return materialItems;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			close(rs, statement, connection);
		}
	}

//	public NewsItem find(Long id) {
//		ResultSet rs = null;
//		PreparedStatement statement = null;
//		Connection connection = null;
//		try {
//			connection = getConnection();
//			String sql = "select * from news_item where id=?";
//			statement = connection.prepareStatement(sql);
//			statement.setLong(1, id.longValue());
//			rs = statement.executeQuery();
//			if (!rs.next()) {
//				return null;
//			}
//			return read(rs);
//		} catch (SQLException e) {
//			throw new RuntimeException(e);
//		} finally {
//			close(rs, statement, connection);
//		}
//	}

}
