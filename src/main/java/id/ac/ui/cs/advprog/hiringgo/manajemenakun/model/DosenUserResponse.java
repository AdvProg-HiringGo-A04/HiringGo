package id.ac.ui.cs.advprog.hiringgo.manajemenakun.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DosenUserResponse extends BaseUserResponse {
    private String namaLengkap;
    private String nip;
}