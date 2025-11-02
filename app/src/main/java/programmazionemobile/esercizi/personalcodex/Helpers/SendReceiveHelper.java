package programmazionemobile.esercizi.personalcodex.Helpers;

import java.util.ArrayList;

import programmazionemobile.esercizi.personalcodex.Database.AsyncAccess.BondsAccess;
import programmazionemobile.esercizi.personalcodex.Database.AsyncAccess.CampaignSectionsAccess;
import programmazionemobile.esercizi.personalcodex.Database.AsyncAccess.CampaignsAccess;
import programmazionemobile.esercizi.personalcodex.Database.AsyncAccess.EntitiesAccess;
import programmazionemobile.esercizi.personalcodex.Database.Entities.FD01_CAMPAIGNS;
import programmazionemobile.esercizi.personalcodex.Database.Entities.FD02_CAMPAIGNS_SECTIONS;
import programmazionemobile.esercizi.personalcodex.Database.Entities.FD03_ENTITIES;

public class SendReceiveHelper {

    private final CampaignsAccess campaignsAccess;
    private final CampaignSectionsAccess campaignSectionsAccess;
    private final EntitiesAccess entitiesAccess;
    private final BondsAccess bondsAccess;

    public SendReceiveHelper(CampaignsAccess campaignsAccess, CampaignSectionsAccess campaignSectionsAccess, EntitiesAccess entitiesAccess, BondsAccess bondsAccess) {
        this.campaignsAccess = campaignsAccess;
        this.campaignSectionsAccess = campaignSectionsAccess;
        this.entitiesAccess = entitiesAccess;
        this.bondsAccess = bondsAccess;
    }

    public  byte[] SendCampaign(long id){
        FD01_CAMPAIGNS campaign = campaignsAccess.get(id);
        ArrayList<FD02_CAMPAIGNS_SECTIONS> sections = campaignSectionsAccess.getAll(id);

        ArrayList<FD03_ENTITIES> entities = new ArrayList<>();
        for(FD02_CAMPAIGNS_SECTIONS section : sections)
            entities.addAll(entitiesAccess.getAll(section.ID));


    }

}
