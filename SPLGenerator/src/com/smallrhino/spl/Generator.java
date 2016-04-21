package com.smallrhino.spl;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

public class Generator {
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Schema schema = new Schema(1, "com.splmeter.entities");
		schema.setDefaultJavaPackageDao("com.splmeter.dao");
		schema.enableKeepSectionsByDefault();
		addData(schema);
		new DaoGenerator().generateAll(schema, "../SPLMeter/src-models");
	}

	private static void addData(Schema schema) {
		/****声源*******/
		Entity equipMentEntity = schema.addEntity("SoundSource");
		equipMentEntity.addIntProperty("ssi_id").unique();
		equipMentEntity.addStringProperty("ssi_code");
		equipMentEntity.addStringProperty("ssi_type");
		equipMentEntity.addStringProperty("ssi_item_cn");
		equipMentEntity.addStringProperty("ssi_item_en");

		/****所在地*******/
		Entity modeEntity = schema.addEntity("Mode");
		modeEntity.addIntProperty("mode_id").unique();
		modeEntity.addStringProperty("mode_code");
		modeEntity.addStringProperty("mode_name_cn");
		modeEntity.addStringProperty("mode_name_en");

		/****asmtValue*******/
		Entity asmtValueEntity = schema.addEntity("AsmtValue");
		asmtValueEntity.addIdProperty().autoincrement();
		asmtValueEntity.addStringProperty("imei");
		asmtValueEntity.addStringProperty("modeltype");
		asmtValueEntity.addStringProperty("mode");
		asmtValueEntity.addStringProperty("source");
		asmtValueEntity.addStringProperty("asmt");
		asmtValueEntity.addIntProperty("gender");
		asmtValueEntity.addIntProperty("age");
		asmtValueEntity.addFloatProperty("mF");
		asmtValueEntity.addFloatProperty("mLpa");
		asmtValueEntity.addFloatProperty("l10");
		asmtValueEntity.addFloatProperty("l50");
		asmtValueEntity.addFloatProperty("l90");
		asmtValueEntity.addFloatProperty("laeq");
		asmtValueEntity.addFloatProperty("calb");
		asmtValueEntity.addIntProperty("post");

		/****SPLValue*******/
		Entity splValueEntity = schema.addEntity("SPLValue");
		splValueEntity.addIdProperty().autoincrement();
		splValueEntity.addDateProperty("time");
		splValueEntity.addIntProperty("earphone");
		splValueEntity.addFloatProperty("lng");
		splValueEntity.addFloatProperty("lat");
		splValueEntity.addFloatProperty("alt");
		splValueEntity.addFloatProperty("acc");
		splValueEntity.addFloatProperty("spl");
		splValueEntity.addFloatProperty("mainF");
		splValueEntity.addFloatProperty("maxLpa");
		Property splvalue_asmt_id = splValueEntity.addLongProperty("asmt_id").notNull().getProperty();
		splValueEntity.addToOne(asmtValueEntity, splvalue_asmt_id);
		ToMany asmtSplValue = asmtValueEntity.addToMany(splValueEntity, splvalue_asmt_id);
		asmtSplValue.setName("splValueList");
	}
}
