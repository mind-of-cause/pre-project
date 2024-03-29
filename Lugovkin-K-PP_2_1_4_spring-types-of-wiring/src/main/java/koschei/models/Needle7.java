package koschei.models;

import org.springframework.stereotype.Component;

@Component
public class Needle7 {
    private Deth8 deth;

    public Needle7(Deth8 deth) {
        this.deth = deth;
    }

    @Override
    public String toString() {
        return ", смерть Кощея на игле :( ";
    }
}