package ac.vibration.util.builtIn;

import ac.vibration.morse.MorseCode;
import ac.vibration.types.Preset;
import ac.vibration.types.PresetList;
import ac.vibration.types.Vib;
import ac.vibration.util.config.PresetsConfig;

public class defVibBuiltIn {

	/**
	 * Guarda las vibraciones predefinidas al iniciarse la aplicaci�n
	 */
	public static void builtInDefVib(){
		final PresetList pl;

		try {
			pl = new PresetsConfig().loadPresets();

			//[morse]sms
			if (pl.getPresetByName("*[Morse] SMS") == null){
				pl.add(new Preset("*[Morse] SMS",new Vib(MorseCode.stringToVib("sms",150, 2))));

			}

			//Long
			if (pl.getPresetByName("*Long") == null){
				long auxLong[] ={ 300, 1000};
				pl.add(new Preset("*Long",new Vib(auxLong)));

			}

			//Looong
			if (pl.getPresetByName("*Looong") == null){
				long auxLong[] ={ 400, 2500};
				pl.add(new Preset("*Looong",new Vib(auxLong)));

			}

			//Long in the middle
			if (pl.getPresetByName("*Long in the middle") == null){
				long auxLong[] ={ 300, 70,50,70,50,70,50,70,50,70,50,70,50,70,50,600,70,50,70,50,70,50,70,50,70,50,70,50,70,};
				pl.add(new Preset("*Long in the middle",new Vib(auxLong)));

			}


			//Long in the middle
			if (pl.getPresetByName("*UCI") == null){
				long auxLong[] ={ 300, 70,50,70,50,70,50,70,50,70,50,70,50,70,50,70,50,70,50,70,50,70,50,70,50,70,50,70,50,70,300};
				pl.add(new Preset("*UCI",new Vib(auxLong)));

			}



			//2 times
			if (pl.getPresetByName("*Twice") == null){
				long auxLong[] ={ 300, 250, 200, 450};
				pl.add(new Preset("*Twice",new Vib(auxLong)));

			}

			//3 times
			if (pl.getPresetByName("*3 times") == null){
				long auxLong[] ={ 300, 200, 150, 250, 190,  650};
				pl.add(new Preset("*3 times",new Vib(auxLong)));

			}




			//brr brr brr brrrrr
			if (pl.getPresetByName("*brr brr brr brrrrr") == null){
				long auxLong[] ={ 300, 250, 80, 250,80, 260, 100, 550};
				pl.add(new Preset("*brr brr brr brrrrr",new Vib(auxLong)));

			}


			//brr brr brr
			if (pl.getPresetByName("*brr brr brr") == null){
				long auxLong[] ={ 300, 250, 80, 250,80, 325};
				pl.add(new Preset("*brr brr brr",new Vib(auxLong)));

			}

			//brr brr brr SuperShort
			if (pl.getPresetByName("*brr brr brr short") == null){
				long auxLong[] ={ 300, 125, 40, 125,40, 180};
				pl.add(new Preset("*brr brr brr short",new Vib(auxLong)));

			}

			//brr brr brr Fast
			if (pl.getPresetByName("*brr brr brr fast") == null){
				long auxLong[] ={ 300, 170, 40, 170,55, 260};
				pl.add(new Preset("*brr brr brr fast",new Vib(auxLong)));

			}

			//brr brr brr Long
			if (pl.getPresetByName("*brr brr brr long") == null){
				long auxLong[] ={ 300, 375, 80, 400, 90 , 450};
				pl.add(new Preset("*brr brr brr long",new Vib(auxLong)));

			}

			//Blink
			if (pl.getPresetByName("*Blink") == null){
				long auxLong[] ={ 300, 100, 65,100, 65,100, 65,100, 65,100, 65,100, 65,100, 65,100, 65,100, 65,100, 65,100, 65,100, 65,100, 65,100, 65,300};
				pl.add(new Preset("*Blink",new Vib(auxLong)));

			}

			//Blink
			if (pl.getPresetByName("*Blink Fast") == null){
				long auxLong[] ={ 300, 90, 32, 90, 32, 90, 32, 90, 32, 90, 32, 90, 32, 90, 32, 90, 32, 90, 32, 90, 32, 90, 32, 90, 32, 90, 32, 65,300};
				pl.add(new Preset("*Blink Fast",new Vib(auxLong)));

			}

			//InCrescendo
			if (pl.getPresetByName("*In Crescendo") == null){
				long auxLong[] ={ 300, 85, 70 ,119 , 75 , 166,80 ,235, 87, 327, 95,457,102, 640};
				pl.add(new Preset("*In Crescendo",new Vib(auxLong)));

			}




			new PresetsConfig().dumpPresetList(pl);


		} catch (Exception e) {
			e.printStackTrace();
		} 
	}



	/**
	 * Guarda las vibraciones predefinidas al iniciarse la aplicaci�n
	 * Este m�todo fuerza a guardar las vibraciones, �til para
	 * actualizaciones del programa.
	 */
	public static void builtInDefVibForce(){
		final PresetList pl;

		try {
			pl = new PresetsConfig().loadPresets();

			pl.add(new Preset("*[morse]sms",new Vib(MorseCode.stringToVib("sms",150, 2))));


			//Long
			long auxLong[] ={ 300, 1000};
			pl.add(new Preset("*Long",new Vib(auxLong)));


			//Looong
			long auxLong2[] ={ 400, 2500};
			pl.add(new Preset("*Looong",new Vib(auxLong2)));

			//Long in the middle
			long auxLong3[] ={ 300, 70,50,70,50,70,50,70,50,70,50,70,50,70,50,600,70,50,70,50,70,50,70,50,70,50,70,50,70,};
			pl.add(new Preset("*Long in the middle",new Vib(auxLong3)));


			//Long in the middle
			long auxLong4[] ={ 300, 70,50,70,50,70,50,70,50,70,50,70,50,70,50,70,50,70,50,70,50,70,50,70,50,70,50,70,50,70,300};
			pl.add(new Preset("*UCI",new Vib(auxLong4)));





			//2 times
			long auxLong5[] ={ 300, 250, 200, 450};
			pl.add(new Preset("*2 times",new Vib(auxLong5)));



			//3 times
			long auxLong6[] ={ 300, 200, 150, 250, 190,  650};
			pl.add(new Preset("*3 times",new Vib(auxLong6)));






			//brr brr brr brrrrr
			long auxLong7[] ={ 300, 250, 80, 250,80, 260, 100, 550};
			pl.add(new Preset("*brr brr brr brrrrr",new Vib(auxLong7)));




			//brr brr brr
			long auxLong8[] ={ 300, 250, 80, 250,80, 325};
			pl.add(new Preset("*brr brr brr",new Vib(auxLong8)));


			//brr brr brr SuperShort
			long auxLong9[] ={ 300, 125, 40, 125,40, 180};
			pl.add(new Preset("*brr brr brr SuperShort",new Vib(auxLong9)));



			//brr brr brr Fast
			long auxLong10[] ={ 300, 170, 40, 170,55, 260};
			pl.add(new Preset("*brr brr brr Fast",new Vib(auxLong10)));



			//brr brr brr Long
			long auxLong11[] ={ 300, 375, 80, 400, 90 , 450};
			pl.add(new Preset("*brr brr brr Long",new Vib(auxLong11)));



			//Blink
			long auxLong12[] ={ 300, 100, 65,100, 65,100, 65,100, 65,100, 65,100, 65,100, 65,100, 65,100, 65,100, 65,100, 65,100, 65,100, 65,100, 65,300};
			pl.add(new Preset("*Blink",new Vib(auxLong12)));


			//Blink
			long auxLong13[] ={ 300, 90, 32, 90, 32, 90, 32, 90, 32, 90, 32, 90, 32, 90, 32, 90, 32, 90, 32, 90, 32, 90, 32, 90, 32, 90, 32, 65,300};
			pl.add(new Preset("*Blink Fast",new Vib(auxLong13)));


			//InCrescendo
			long auxLong14[] ={ 300, 85, 70 ,119 , 75 , 166,80 ,235, 87, 327, 95,457,102, 640};
			pl.add(new Preset("*In Crescendo",new Vib(auxLong14)));





			new PresetsConfig().dumpPresetList(pl);


		} catch (Exception e) {
			e.printStackTrace();
		} 
	}



}
