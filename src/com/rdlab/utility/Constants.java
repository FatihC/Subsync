package com.rdlab.utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Constants {
	public static final String FOR_CONTROL = "FOR_CONTROL";
	
	public static final String CITY_CODE = "63";
	public static final String COUNTY_CODE_TAG = "COUNTY_CODE";
	public static final String DISTRICT_CODE_TAG = "DISTRICT_CODE";
	public static final String VILLAGE_CODE_TAG = "VILLAGE_CODE";
	public static final String STREET_CODE_TAG = "STREET_CODE";
	public static final String CSBM_CODE_TAG = "CSBM_CODE";
	public static final String DOOR_NUMBER_TAG = "DOOR_NUMBER";
	public static final String SITE_NAME_TAG = "SITE_NAME";
	public static final String BLOCK_NAME_TAG = "BLOCK_NAME";
	public static final String UAVT_TAG = "UAVT_ADDRESS_NO";
	public static final String INDOOR_TAG = "INDOOR_NUMBER";
	public static final String CHECKED_UAVT = "CHECK_STATUS";
	public static final String SELECTED_COUNTY_CODE = "SELECTED_COUNTY_CODE";
	public static final String SELECTED_COUNTY_NAME = "SELECTED_COUNTY_NAME";
	public static final String SELECTED_COUNTY_DB_CODE = "SELECTED_COUNTY_DB_CODE";
	public static final String SELECTED_CLASS_NAME = "SELECTED_CLASS_NAME";

	public static final String EDIT_OUTDOOR = "OUTDOOR_NUM_EDIT";
	public static final String EDIT_SITE = "SITE_NAME_EDIT";
	public static final String EDIT_BLOCK = "BLOCK_NAME_EDIT";
	public static final String EDIT_INDOOR = "EDIT_INDOOR";

	public static final String DISTRICT_NAME_TAG = "DISTRICT_NAME";
	public static final String VILLAGE_NAME_TAG = "VILLAGE_NAME";
	public static final String STREET_NAME_TAG = "STREET_NAME";
	public static final String CSBM_NAME_TAG = "CSBM_NAME";

	public static final String HOME_LIST_STATE = "HOME_LIST_STATE";
	public static final String DISTRICT_LIST_STATE = "DISTRICT_LIST_STATE";
	public static final String VILLAGE_LIST_STATE = "VILLAGE_LIST_STATE";
	public static final String STREET_LIST_STATE = "STREET_LIST_STATE";
	public static final String CSBM_LIST_STATE = "CSBM_LIST_STATE";

	public static final String HALILIYE = "1";
	public static final String EYYUBIYE = "2";
	public static final String VIRANSEHIR = "3";
	public static final String SIVEREK = "4";
	public static final String KARAKOPRU = "5";
	public static final String BIRECIK = "6";
	public static final String SURUC = "7";
	public static final String AKCAKALE = "8";
	public static final String BOZOVA = "9";
	public static final String CEYLANPINAR = "10";
	public static final String HALFETI = "11";
	public static final String HARRAN = "12";
	public static final String HILVAN = "13";
	public static final String INITIAL_SYNC_DATE = "20141215000000";
	
	/*
	 * Configuration Keys
	 */
	public static final String LAST_SYNC_TAG = "LAST_SYNC_DATE";
	public static final String LAST_PUSH_TAG = "LAST_PUSH_DATE";

	/*
	 * Fragments Headers
	 */
	public static final String DISTRICT_HEADER_TEXT = "L�TFEN MAHALLE SE��N�Z";
	public static final String VILLAGE_HEADER_TEXT = "L�TFEN K�Y SE��N�Z";
	public static final String STREET_HEADER_TEXT = "L�TFEN MAHALLE/K�Y SE��N�Z";
	public static final String CSBM_HEADER_TEXT = "L�TFEN CADDE/SOKAK/BULVAR/MEYDAN/MEZRA SE��N�Z";
	public static final String BLOCK_HEADER_TEXT = "L�TFEN DI� KAPI NUMARASI SE��N�Z";
	public static final String UNIT_HEADER_TEXT = "L�TFEN �� KAPI NO SE��N�Z";
	public static final String AUDIT_LOG_LIST = "TESP�T L�STES�";
	public static final String AUDIT_LOG_FORM = "L�TFEN TESP�T VER�LER�N� DOLDURUNUZ";
	public static final String MATCH_TEXT = "E�LE�T�RME";
	public static final String SYNC_HEADER_TEXT = "VER� G�NCELLEME";

	/*
	 * Warnings
	 */
	public static final String USER_NOT_EXIST = "Kullan�c� Ad� ya da �ifre hatas�.";
	public static final String NO_ITEM_SELECTED = "Tesisat No, Saya� No-Marka veya Durum se�ilmelidir.";

	/*
	 * Meter brands
	 */
	public static final ArrayList<String> METER_BRANDS = new ArrayList<String>(
			Arrays.asList("", "ABB", "AEG", "ALFATECH", "AMPY", "ASER",
					"ASSAY ECHELON", "BA�ARI - KAAN", "BAYLAN", "Circulator",
					"EAS M1", "Ekosay", "ELEKTRA", "ELEKTROMED", "EMH ELGAMA",
					"Entes", "ESEM", "FEDERAL", "FLASH", "GANZ", "ISKRA",
					"�SRA", "K�HLER", "K�HLER (AEL)", "L�BRA", "LUNA",
					"M.K.E.", "MAKEL", "MATE�", "MEKS", "Merlin Gerin", "NORA",
					"ONUR", "ORBIS", "Profilo", "PROTON", "SIEMENS",
					"Telefunken", "Vi-KO", "Diger"));

	public static final ArrayList<String> UNIT_STATUS = new ArrayList<String>(
			Arrays.asList("Ortak Kullan�m", "Ev", "�� Yeri", "Resmi Kurum",
					"Ambar", "Bodrum", "Depo", "Hidrofor", "Kazan Dairesi"));

	public static final ArrayList<String> STATUSES = new ArrayList<String>(
			Arrays.asList("", "Birim Yok", "Kapal�", "Saya� Var Ula��lamad�",
					"Saya� yok - Enerjisiz Birim",
					"Saya� Yok - Ortak Kullan�m", "Saya� Yok - S�zme Saya�",
					"Saya� Yok - Abonesiz"));
	
	public static final ArrayList<String> AUDITSELECTION = new ArrayList<String>(
			Arrays.asList("Zaten Abone - Sayac� MBS�de G�ncel", 
					"Zaten Abone - Sayac� MBS�de G�ncel De�il", 
					"Abone De�il � Abone Yap�ld�", 
					"Abone De�il � Abone Yap�lamad�",
					"Kontrol Edildi � Girilen Bilgiler Do�ru",
					"Abone Yap�lmas�na Gerek Yok",
					"Veri g�ncellemesi yap�ld�"));
	
	public static final ArrayList<String> AUDITPROGRESSTYPE = new ArrayList<String>(
			Arrays.asList("-","Devam Ediyor", "Tamamland�"));

	public static String SelectedCountyCode = "1115";
	public static String SelectedUniversalCountyCode = "35";
	public static String SelectedCountyName = "Ak�akale";
	public static String SelectedClassName= "com.rdlab.model.Akcakale";
	public static String SelectedDistrictCode= "";
	public static String SelectedDistrictName = "";
	public static String SelectedVillageCode = "";
	public static String SelectedVillageName = "";
	public static boolean Log4JConfigured=false;
	
	
	
	public static Long LoggedUserSerno;
	public static String LoggedUserName;
	public static String LoggedUserFullname;

	public static Map<String, String> COUNTY_CODES = new HashMap<String, String>();
	public static Map<String, String> METER_BRAND_CODES = new HashMap<String, String>();

	static {
		COUNTY_CODES.put("32", "com.rdlab.model.Eyyubiye");
		COUNTY_CODES.put("33", "com.rdlab.model.Haliliye");
		COUNTY_CODES.put("34", "com.rdlab.model.Karakopru");
		COUNTY_CODES.put("35", "com.rdlab.model.Akcakale");
		COUNTY_CODES.put("36", "com.rdlab.model.Birecik");
		COUNTY_CODES.put("37", "com.rdlab.model.Bozova");
		COUNTY_CODES.put("38", "com.rdlab.model.Ceylanpinar");
		COUNTY_CODES.put("39", "com.rdlab.model.Halfeti");
		COUNTY_CODES.put("40", "com.rdlab.model.Harran");
		COUNTY_CODES.put("41", "com.rdlab.model.Hilvan");
		COUNTY_CODES.put("42", "com.rdlab.model.Siverek");
		COUNTY_CODES.put("43", "com.rdlab.model.Suruc");
		COUNTY_CODES.put("44", "com.rdlab.model.Viransehir");

		METER_BRAND_CODES.put("AEG", "AEG");
		METER_BRAND_CODES.put("K�HLER (AEL)", "AEL");
		METER_BRAND_CODES.put("MATE�", "MAE");
		METER_BRAND_CODES.put("Vi-KO", "VIK");
		METER_BRAND_CODES.put("ASSAY ECHELON", "ASY");
		METER_BRAND_CODES.put("ELEKTRA", "ELK");
		METER_BRAND_CODES.put("EMH ELGAMA", "EMH");
		METER_BRAND_CODES.put("�SRA", "ISR");
		METER_BRAND_CODES.put("ABB", "ABB");
		METER_BRAND_CODES.put("FLASH", "FLS");
		METER_BRAND_CODES.put("M.K.E.", "MKE");
		METER_BRAND_CODES.put("MAKEL", "MSY");
		METER_BRAND_CODES.put("MEKS", "MEK");
		METER_BRAND_CODES.put("NORA", "NES");
		METER_BRAND_CODES.put("ONUR", "ONR");
		METER_BRAND_CODES.put("ORBIS", "ORB");
		METER_BRAND_CODES.put("ALFATECH", "ALF");
		METER_BRAND_CODES.put("AMPY", "AMP");
		METER_BRAND_CODES.put("ASER", "ASR");
		METER_BRAND_CODES.put("PROTON", "PRO");
		METER_BRAND_CODES.put("SIEMENS", "SMS");
		METER_BRAND_CODES.put("EAS M1", "EAS");
		METER_BRAND_CODES.put("Entes", "ENT");
		METER_BRAND_CODES.put("ISKRA", "ISK");
		METER_BRAND_CODES.put("L�BRA", "LBR");
		METER_BRAND_CODES.put("Circulator", "CIR");
		METER_BRAND_CODES.put("ESEM", "ESI");
		METER_BRAND_CODES.put("FEDERAL", "FED");
		METER_BRAND_CODES.put("GANZ", "GNZ");
		METER_BRAND_CODES.put("K�HLER", "KHL");
		METER_BRAND_CODES.put("LUNA", "LUN");
		METER_BRAND_CODES.put("Merlin Gerin", "MER");
		METER_BRAND_CODES.put("Profilo", "PRF");
		METER_BRAND_CODES.put("BA�ARI - KAAN", "BSE");
		METER_BRAND_CODES.put("Diger", "   ");
		METER_BRAND_CODES.put("Ekosay", "NRM");
		METER_BRAND_CODES.put("ELEKTROMED", "ELM");
		METER_BRAND_CODES.put("Telefunken", "TLF");
		METER_BRAND_CODES.put("BAYLAN", "BYL");
	}

}
