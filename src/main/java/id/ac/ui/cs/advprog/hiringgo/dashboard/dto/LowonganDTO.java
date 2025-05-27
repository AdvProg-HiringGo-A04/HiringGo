
package id.ac.ui.cs.advprog.hiringgo.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LowonganDTO {
    private String id;
    private String mataKuliahName;
    private String tahunAjaran;
    private String semester;
}