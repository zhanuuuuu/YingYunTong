package Tool;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.cloopen.rest.sdk.utils.LoggerUtil;

import DB.DB;

public class GetcSheetno {

	public static String getcSheetno(Connection conn, String year, String no) { // 补货单号
		CallableStatement c = null;
		ResultSet rs = null;
		String getSheetno = null;
		try {
			c = conn.prepareCall("{call getSheetno (?,?)}");
			c.setString(1, year);
			c.setString(2, no);
			rs = c.executeQuery();
			while (rs.next()) {
				getSheetno = rs.getString("Sheetno");
			}
			return getSheetno;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closeCallState(c);
			DB.closeResultSet(rs);
		}
		return null;
	}

	public static String getcSheetno_Fen_jian(Connection conn, String year, String no) { // 分拣单号
		CallableStatement c = null;
		ResultSet rs = null;
		String fenjian_no = null;
		try {
			c = conn.prepareCall("{call fenjian_no (?,?)}");
			c.setString(1, year);
			c.setString(2, no);
			rs = c.executeQuery();
			while (rs.next()) {
				fenjian_no = rs.getString("fenjian_no");
			}
			return fenjian_no;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closeCallState(c);
			DB.closeResultSet(rs);
		}
		return null;
	}

	public static String getGenPalletSheet_no(Connection conn, String year, String month) { // 拖盘单号
		CallableStatement c = null;
		ResultSet rs = null;
		String PalletSheet_no = null;
		try {
			c = conn.prepareCall("{call GenPalletSheet (?,?)}");
			c.setString(1, year);
			c.setString(2, month);
			rs = c.executeQuery();
			while (rs.next()) {
				PalletSheet_no = rs.getString("PalletSheet_no");
			}
			return PalletSheet_no;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closeCallState(c);
			DB.closeResultSet(rs);
		}
		return null;
	}

	public static String getGenPei_Song_Yan_Huo_dan_no(Connection conn, String year, String cCustomerNo) { // 配送验货单号
		CallableStatement c = null;
		ResultSet rs = null;
		String PalletSheet_no = null;
		try {
			c = conn.prepareCall("{call getPei_song_yan_no (?,?)}");
			c.setString(1, year);
			c.setString(2, cCustomerNo);
			rs = c.executeQuery();
			while (rs.next()) {
				PalletSheet_no = rs.getString("Pei_song_yan_no");
			}
			return PalletSheet_no;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closeCallState(c);
			DB.closeResultSet(rs);
		}
		return null;
	}

	public static String get_peisong_cha_yi_no(Connection conn, String year, String cstoreno) { // 差异单号
		CallableStatement c = null;
		ResultSet rs = null;
		String zhuang_che_Sheetno = null;
		try {
			c = conn.prepareCall("{call  get_peisong_cha_yi_dan_no (?,?)}");
			c.setString(1, year);
			c.setString(2, cstoreno);
			rs = c.executeQuery();
			while (rs.next()) {
				zhuang_che_Sheetno = rs.getString("peisong_cha_yi_dan_no");
			}
			return zhuang_che_Sheetno;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closeCallState(c);
			DB.closeResultSet(rs);
		}
		return null;
	}

	public static String get_zhuang_che_Sheetno(Connection conn, String year, String month) { // 装车单号
		CallableStatement c = null;
		ResultSet rs = null;
		String zhuang_che_Sheetno = null;
		try {
			c = conn.prepareCall("{call get_zhuang_che_Sheetno (?,?)}");
			c.setString(1, year);
			c.setString(2, month);
			rs = c.executeQuery();
			while (rs.next()) {
				zhuang_che_Sheetno = rs.getString("zhuang_che_Sheetno");
			}
			return zhuang_che_Sheetno;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closeCallState(c);
			DB.closeResultSet(rs);
		}
		return null;
	}

	public static String getfen_dian_yan_zong_bu_no(Connection conn, String year, String fen_dian) { // 分店验总部
		CallableStatement c = null;
		ResultSet rs = null;
		String zhuang_che_Sheetno = null;
		try {
			c = conn.prepareCall("{call fen_dian_yan_zong_bu_no (?,?)}");// 1
			c.setString(1, year);
			c.setString(2, fen_dian);
			rs = c.executeQuery();
			while (rs.next()) {
				zhuang_che_Sheetno = rs.getString("fen_dian_yan_zong_bu_no");
			}
			return zhuang_che_Sheetno;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closeCallState(c);
			DB.closeResultSet(rs);
		}
		return null;
	}

	public static String getfen_dian_ru_ku_dan_no(Connection conn, String year, String fen_dian) { // 分店入库
		CallableStatement c = null;
		ResultSet rs = null;
		String zhuang_che_Sheetno = null;

		try {
			c = conn.prepareCall("{call fen_dian_ru_ku_dan_no (?,?)}");
			c.setString(1, year);
			c.setString(2, fen_dian);
			rs = c.executeQuery();
			while (rs.next()) {
				zhuang_che_Sheetno = rs.getString("fen_dian_ru_ku_dan_no");
			}
			return zhuang_che_Sheetno;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closeCallState(c);
			DB.closeResultSet(rs);
		}
		return null;
	}

	public static String getfen_dian_tui_huo_no(Connection conn, String year, String cStoreno) { // 分店退货单
		CallableStatement c = null;
		ResultSet rs = null;
		String fen_dian_tui_huo_no = null;
		try {
			c = conn.prepareCall("{call fen_dian_tui_huo_dan_no (?,?)}");
			c.setString(1, year);
			c.setString(2, cStoreno);
			rs = c.executeQuery();
			while (rs.next()) {
				fen_dian_tui_huo_no = rs.getString("fen_dian_tui_huo_no");
			}
			return fen_dian_tui_huo_no;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closeCallState(c);
			DB.closeResultSet(rs);
		}
		return null;
	}

	public static String get_diao_bo_chu_ku_no(Connection conn, String year, String cWhno) { // 分店退货单
		CallableStatement c = null;
		ResultSet rs = null;
		String diao_bo_chu_ku_no = null;
		try {
			c = conn.prepareCall("{call diao_bo_chu_ku_no (?,?)}");
			c.setString(1, year);
			c.setString(2, cWhno);
			rs = c.executeQuery();
			while (rs.next()) {
				diao_bo_chu_ku_no = rs.getString("diao_bo_chu_ku_no");
			}
			return diao_bo_chu_ku_no;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closeCallState(c);
			DB.closeResultSet(rs);
		}
		return null;
	}

	public static String get_diao_bo_ru_ku_no(Connection conn, String year, String cWhno) { // 分店退货单
		CallableStatement c = null;
		ResultSet rs = null;
		String diao_bo_ru_ku_no = null;
		try {
			c = conn.prepareCall("{call diao_bo_ru_ku_no (?,?)}");
			c.setString(1, year);
			c.setString(2, cWhno);
			rs = c.executeQuery();
			while (rs.next()) {
				diao_bo_ru_ku_no = rs.getString("diao_bo_ru_ku_no");
			}
			return diao_bo_ru_ku_no;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closeCallState(c);
			DB.closeResultSet(rs);
		}
		return null;
	}

	public static String get_bo_sun_no(Connection conn, String year, String Storeno) { // 分店退货单
		CallableStatement c = null;
		ResultSet rs = null;
		String diao_bo_ru_ku_no = null;
		try {
			c = conn.prepareCall("{call bao_sun_no (?,?)}");
			c.setString(1, year);
			c.setString(2, Storeno);
			rs = c.executeQuery();
			while (rs.next()) {
				diao_bo_ru_ku_no = rs.getString("bao_sun_no");
			}
			return diao_bo_ru_ku_no;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closeCallState(c);
			DB.closeResultSet(rs);
		}
		return null;
	}

	public static String get_bo_yi_no(Connection conn, String year, String Storeno) { // 分店报溢
		CallableStatement c = null;
		ResultSet rs = null;
		String diao_bo_ru_ku_no = null;
		try {
			c = conn.prepareCall("{call bao_yi_no (?,?)}");
			c.setString(1, year);
			c.setString(2, Storeno);
			rs = c.executeQuery();
			while (rs.next()) {
				diao_bo_ru_ku_no = rs.getString("bao_yi_no");
			}
			return diao_bo_ru_ku_no;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closeCallState(c);
			DB.closeResultSet(rs);
		}
		return null;
	}

	public static String get_vip_no(Connection conn, String year, String Storeno) { // vip价格
		CallableStatement c = null;
		ResultSet rs = null;
		String diao_bo_ru_ku_no = null;
		try {
			c = conn.prepareCall("{call vip_price_no (?,?)}");
			c.setString(1, year);
			c.setString(2, Storeno);
			rs = c.executeQuery();
			while (rs.next()) {
				diao_bo_ru_ku_no = rs.getString("vip_no_price_no");
			}
			return diao_bo_ru_ku_no;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closeCallState(c);
			DB.closeResultSet(rs);
		}
		return null;
	}

	public static String get_men_dian_zhi_pei_no(Connection conn, String year, String cSupplierNo) { // 分店直配
		CallableStatement c = null;
		ResultSet rs = null;
		String fen_dian_tui_huo_no = null;
		try {
			c = conn.prepareCall("{call men_dian_zhi_pei_no (?,?)}");
			c.setString(1, year);
			c.setString(2, cSupplierNo);
			rs = c.executeQuery();
			while (rs.next()) {
				fen_dian_tui_huo_no = rs.getString("men_dian_zhi_pei_no");
			}
			return fen_dian_tui_huo_no;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closeCallState(c);
			DB.closeResultSet(rs);
		}
		return null;
	}

	public static String get_tui_huo_dao_gong_ying_shang_no(Connection conn, String year, String cSupplierNo) { // 分店退货到供应商
		CallableStatement c = null;
		ResultSet rs = null;
		String fen_dian_tui_huo_no = null;
		try {
			c = conn.prepareCall("{call tui_huo_dao_gong_ying_shang_no (?,?)}");
			c.setString(1, year);
			c.setString(2, cSupplierNo);
			rs = c.executeQuery();
			while (rs.next()) {
				fen_dian_tui_huo_no = rs.getString("tui_huo_dao_gong_ying_shang_no");
			}
			return fen_dian_tui_huo_no;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closeCallState(c);
			DB.closeResultSet(rs);
		}
		return null;
	}

	public static String get_men_dian_chang_jia_cai_gou_no(Connection conn, String year, String cSupplierNo) { // 门店厂家采购
		CallableStatement c = null;
		ResultSet rs = null;
		String men_dian_chang_jia_cai_gou = null;
		try {
			c = conn.prepareCall("{call men_dian_chang_jia_cai_gou (?,?)}");
			c.setString(1, year);
			c.setString(2, cSupplierNo);
			rs = c.executeQuery();
			while (rs.next()) {
				men_dian_chang_jia_cai_gou = rs.getString("men_dian_chang_jia_cai_gou");
			}
			return men_dian_chang_jia_cai_gou;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closeCallState(c);
			DB.closeResultSet(rs);
		}
		return null;
	}

	public static String get_commonality_cSheetNo(Connection conn, String sql) { // 门店厂家采购
		ResultSet rs = null;
		PreparedStatement past = null;
		String cSheetNo = null;
		try {
			past = conn.prepareStatement(sql);
			rs = past.executeQuery();
			if (rs.next()) {
				cSheetNo = rs.getString("SheetNo");
			}
			LoggerUtil.info(cSheetNo);
			DB.closeResultSet(rs);
			DB.closePreparedStatement(past);
			return cSheetNo;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closePreparedStatement(past);
			DB.closeResultSet(rs);
		}
		return null;
	}

}
