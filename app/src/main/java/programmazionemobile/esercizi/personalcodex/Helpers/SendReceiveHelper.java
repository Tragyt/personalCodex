package programmazionemobile.esercizi.personalcodex.Helpers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import programmazionemobile.esercizi.personalcodex.Database.AsyncAccess.BondsAccess;
import programmazionemobile.esercizi.personalcodex.Database.AsyncAccess.CampaignSectionsAccess;
import programmazionemobile.esercizi.personalcodex.Database.AsyncAccess.CampaignsAccess;
import programmazionemobile.esercizi.personalcodex.Database.AsyncAccess.EntitiesAccess;
import programmazionemobile.esercizi.personalcodex.Database.Entities.FD01_CAMPAIGNS;
import programmazionemobile.esercizi.personalcodex.Database.Entities.FD02_CAMPAIGNS_SECTIONS;
import programmazionemobile.esercizi.personalcodex.Database.Entities.FD03_ENTITIES;
import programmazionemobile.esercizi.personalcodex.Database.Entities.FD04_BONDS;

public class SendReceiveHelper {

    public static byte[] CampaingPayload(long id, CampaignsAccess campaignsAccess) throws IOException {
        FD01_CAMPAIGNS campaign = campaignsAccess.get(id);
        ArrayList<FD02_CAMPAIGNS_SECTIONS> sections = campaignsAccess.getAllSections(id);
        ArrayList<FD03_ENTITIES> entities = campaignsAccess.getAllEntities(id);
        ArrayList<FD04_BONDS> bonds = campaignsAccess.getAllBonds(id);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(campaign);
        oos.writeObject(sections);
        oos.writeObject(entities);
        oos.writeObject(bonds);
        oos.flush();
        return bos.toByteArray();
    }

    public static byte[] EntityPayload(long id, EntitiesAccess entitiesAccess) throws IOException {
        FD03_ENTITIES entity = entitiesAccess.get(id);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(entity);
        oos.flush();
        return bos.toByteArray();
    }

    @SuppressWarnings("unchecked")
    public static void ReceiveCampaign(byte[] data, CampaignsAccess campaignsAccess,
                                       CampaignSectionsAccess campaignSectionsAccess,
                                       EntitiesAccess entitiesAccess, BondsAccess bondsAccess)
            throws IOException, ClassNotFoundException {

        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        ObjectInputStream ois = new ObjectInputStream(bais);

        FD01_CAMPAIGNS campaign = (FD01_CAMPAIGNS) ois.readObject();
        ArrayList<FD02_CAMPAIGNS_SECTIONS> sections = (ArrayList<FD02_CAMPAIGNS_SECTIONS>) ois.readObject();
        ArrayList<FD03_ENTITIES> entities = (ArrayList<FD03_ENTITIES>) ois.readObject();
        ArrayList<FD04_BONDS> bonds = (ArrayList<FD04_BONDS>) ois.readObject();

        long new_campaign_id = campaignsAccess.insert(campaign);
        for (FD02_CAMPAIGNS_SECTIONS section : sections) {
            section.FD02_CAMPAIGN_FD01 = new_campaign_id;
            long new_section_id = campaignSectionsAccess.insert(section);

            Map<Long, Long> old_new_entities = new HashMap<>();
            for (FD03_ENTITIES entity : entities) {
                long old_entity_id = entity.ID;
                entity.FD03_SECTION_FD02 = new_section_id;
                long new_entity_id = entitiesAccess.insert(entity);
                old_new_entities.put(old_entity_id, new_entity_id);
            }

            for (FD04_BONDS bond : bonds) {
                bond.FD04_ENTITY1_FD03 = Optional.ofNullable(old_new_entities.get(bond.FD04_ENTITY1_FD03)).orElse(-1L);
                bond.FD04_ENTITY2_FD03 = Optional.ofNullable(old_new_entities.get(bond.FD04_ENTITY2_FD03)).orElse(-1L);
                if(bond.FD04_ENTITY1_FD03>0 && bond.FD04_ENTITY2_FD03>0)
                    bondsAccess.insert(bond);
            }
        }
    }

    public static void ReceiveEntity(byte[] data, long idSection, EntitiesAccess entitiesAccess) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        ObjectInputStream ois = new ObjectInputStream(bais);
        FD03_ENTITIES entity = (FD03_ENTITIES) ois.readObject();
        entity.FD03_SECTION_FD02 = idSection;
        entitiesAccess.insert(entity);
    }

}
