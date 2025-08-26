package programmazionemobile.esercizi.personalcodex.Helpers;

import java.util.ArrayList;

import programmazionemobile.esercizi.personalcodex.Database.Entities.FD02_CAMPAIGNS_SECTIONS;
import programmazionemobile.esercizi.personalcodex.Database.Entities.FD03_ENTITIES;

public class CampaignsHelper {
    public static class SectionEntities {
        private final FD02_CAMPAIGNS_SECTIONS section;
        private final ArrayList<FD03_ENTITIES> entities;
        private Boolean expanded;

        public SectionEntities(FD02_CAMPAIGNS_SECTIONS section, ArrayList<FD03_ENTITIES> entities) {
            this.section = section;
            this.entities = entities;
            this.expanded = false;
        }

        public Boolean isExpanded() {
            return expanded;
        }

        public void expand_reduce() {
            expanded = !expanded;
        }

        public void add(FD03_ENTITIES entity) {
            entities.add(entity);
        }

        public ArrayList<FD03_ENTITIES> getEntities() {
            return entities;
        }

        public void removeEntity(long idEntity){
            entities.removeIf(entity -> entity.ID == idEntity);
        }

        public FD02_CAMPAIGNS_SECTIONS getSection(){
            return section;
        }
    }

    public enum CampaignRole{
        VIEW,
        NEW_LINK
    }
}
