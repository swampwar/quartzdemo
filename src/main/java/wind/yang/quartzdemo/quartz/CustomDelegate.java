package wind.yang.quartzdemo.quartz;

import org.quartz.impl.jdbcjobstore.PostgreSQLDelegate;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.sql.ResultSet;
import java.sql.SQLException;

// TODO OracleDelegate의 소스를 가져와야 한다.
public class CustomDelegate extends CustomStdJDBCDelegate {

    //---------------------------------------------------------------------------
    // protected methods that can be overridden by subclasses
    //---------------------------------------------------------------------------

    /**
     * <p>
     * This method should be overridden by any delegate subclasses that need
     * special handling for BLOBs. The default implementation uses standard
     * JDBC <code>java.sql.Blob</code> operations.
     * </p>
     *
     * @param rs
     *          the result set, already queued to the correct row
     * @param colName
     *          the column name for the BLOB
     * @return the deserialized Object from the ResultSet BLOB
     * @throws ClassNotFoundException
     *           if a class found during deserialization cannot be found
     * @throws IOException
     *           if deserialization causes an error
     */
    @Override
    protected Object getObjectFromBlob(ResultSet rs, String colName)
            throws ClassNotFoundException, IOException, SQLException {
        InputStream binaryInput = null;
        byte[] bytes = rs.getBytes(colName);

        Object obj = null;

        if(bytes != null && bytes.length != 0) {
            binaryInput = new ByteArrayInputStream(bytes);

            ObjectInputStream in = new ObjectInputStream(binaryInput);
            try {
                obj = in.readObject();
            } finally {
                in.close();
            }

        }

        return obj;
    }

    @Override
    protected Object getJobDataFromBlob(ResultSet rs, String colName)
            throws ClassNotFoundException, IOException, SQLException {
        if (canUseProperties()) {
            InputStream binaryInput = null;
            byte[] bytes = rs.getBytes(colName);
            if(bytes == null || bytes.length == 0) {
                return null;
            }
            binaryInput = new ByteArrayInputStream(bytes);
            return binaryInput;
        }
        return getObjectFromBlob(rs, colName);
    }
}
