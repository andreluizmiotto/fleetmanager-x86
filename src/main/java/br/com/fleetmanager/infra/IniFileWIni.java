package br.com.fleetmanager.infra;

import org.ini4j.Wini;
import java.io.File;
import java.io.IOException;

public class IniFileWIni {

    Wini iniFile;

    public IniFileWIni(String pPathName) {
        try{
            File file = new File(pPathName + ".ini");
            if (!file.exists())
                file.createNewFile();
            this.iniFile = new Wini(file);
        } catch (IOException throwables) {
            throw new RuntimeException(throwables);
        }
    }

    public IniFileWIni put(String sectionName, String optionName, Object value) {
        this.iniFile.put(sectionName, optionName, value);
        return this;
    }

    public void store() {
        try{
            this.iniFile.store();
        } catch (IOException throwables) {
            throw new RuntimeException(throwables);
        }
    }

    public Wini getIniFile() {
        return iniFile;
    }
}
