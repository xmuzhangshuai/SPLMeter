package com.smallrhino.spl;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

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
	}
}
