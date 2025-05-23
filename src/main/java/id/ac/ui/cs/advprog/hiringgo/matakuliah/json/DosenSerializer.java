package id.ac.ui.cs.advprog.hiringgo.matakuliah.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import id.ac.ui.cs.advprog.hiringgo.entity.Dosen;

import java.io.IOException;

public class DosenSerializer extends JsonSerializer<Dosen> {

    @Override
    public void serialize(Dosen dosen, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(dosen.getId());
    }
}
