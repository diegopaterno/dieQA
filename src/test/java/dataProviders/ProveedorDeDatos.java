package dataProviders;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.testng.annotations.DataProvider;

import Tools.JsonDataReader;

public class ProveedorDeDatos {

	@DataProvider(name = "")
	public Object[] de22 () {
		Object[][] DE22 = {{"9010"},{"9020"},{"9090"},{"9050"},{"9081"},{"9070"},{"9510"},{"9520"},{"9590"},{"9550"},{"9581"},{"9570"}};
		//Object[][] DE22 = {{"9010"},{"9020"},{"9090"},{"9050"},{"9081"},{"9070"}};
        return DE22;
	};
	@DataProvider(name="dataElement22", parallel = false)
    public Iterator<Object[]> userData() throws IOException {
        // Lee el archivo JSON y obtén la lista de usuarios
        List<Object[]> userData = JsonDataReader.readUserData("./Datasources/datos.json");
        return userData.iterator();
    }
    @DataProvider(name="dataElement22MAS", parallel = false)
    public Iterator<Object[]> userDataMAS() throws IOException {
        // Lee el archivo JSON y obtén la lista de usuarios
        List<Object[]> userDataMAS = JsonDataReader.readUserData("./Datasources/datos3.json");
        return userDataMAS.iterator();
    }
    @DataProvider(name="ambiente", parallel = false)
    public Iterator<Object[]> userData2() throws IOException {
        // Lee el archivo JSON y obtén la lista de usuarios
        List<Object[]> userData = JsonDataReader.readUserData2("./Datasources/datos2.json");
        return userData.iterator();
    }
}
