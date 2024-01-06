import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddressApp {
    public static void main(String[] args) throws IOException {
        // Read data from files
        BufferedReader reader = new BufferedReader(new FileReader("AS_ADDR_OBJ.XML"));
        List<Address> addresses = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            String[] data = line.split(",");
            Address address = new Address();
            address.OBJECTID = Integer.parseInt(data[0]);
            address.NAME = data[1];
            address.TYPENAME = data[2];
            address.STARTDATE = data[3];
            address.ENDDATE = data[4];
            address.ISACTUAL = Boolean.parseBoolean(data[5]);
            addresses.add(address);
        }
        reader.close();

        reader = new BufferedReader(new FileReader("AS_ADM_HIERARCHY.XML"));
        List<Address> addressHierarchy = new ArrayList<>();
        while ((line = reader.readLine()) != null) {
            String[] data = line.split(",");
            Address address = new Address();
            address.OBJECTID = Integer.parseInt(data[0]);
            address.PARENTOBJID = Integer.parseInt(data[1]);
            address.STARTDATE = data[2];
            address.ENDDATE = data[3];
            address.ISACTUAL = Boolean.parseBoolean(data[4]);
            addressHierarchy.add(address);
        }
        reader.close();

        // Задача 1
        String date = "2010-01-01";
        String[] objectIds = {"1422396", "1450759", "1449192", "1451562"};
        printAddressesByDate(addresses, date);

        // Задача 2
        printAddressesWithTypeProjects(addresses, addressHierarchy);
    }

    private static void printAddressesByDate(List<Address> addresses, String date) {
        LocalDate localDate = LocalDate.parse(date);
        List<Address> matchingAddresses = new ArrayList<>();
        for (Address address : addresses) {
            if (address.ISACTUAL && address.STARTDATE.equals(localDate.toString())) {
                matchingAddresses.add(address);
            }
        }
        for (Address address : matchingAddresses) {
            System.out.println(address.OBJECTID + ": " + address.TYPENAME + " " + address.NAME);
        }
    }

    private static void printAddressesWithTypeProjects(List<Address> addresses, List<Address> addressHierarchy) {
        List<Address> matchingAddresses = new ArrayList<>();
        Map<Integer, Address> hierarchyMap = new HashMap<>();
        for (Address address : addressHierarchy) {
            hierarchyMap.put(address.OBJECTID, address);
        }
        for (Address address : addresses) {
            if (address.TYPENAME.equals("проезд")) {
                matchingAddresses.add(address);
            }
        }
        for (Address address : matchingAddresses) {
            Address parentAddress = hierarchyMap.get(address.PARENTOBJID);
            if (parentAddress != null) {
                System.out.println(parentAddress.NAME + " " + parentAddress.TYPENAME + " " + address.NAME + " " + address.TYPENAME);
            }
        }
    }
}

class Address {
    int OBJECTID;
    String NAME;
    String TYPENAME;
    String STARTDATE;
    String ENDDATE;
    boolean ISACTUAL;
    int PARENTOBJID;
}
