package id.ac.ui.cs.advprog.hiringgo.matakuliah.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import id.ac.ui.cs.advprog.hiringgo.entity.Dosen;

import java.io.IOException;

public class DosenDeserializer extends JsonDeserializer<Dosen> {

    @Override
    public Dosen deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String id = p.getValueAsString();
        Dosen dosen = new Dosen();
        dosen.setId(id);
        return dosen;
    }
}
