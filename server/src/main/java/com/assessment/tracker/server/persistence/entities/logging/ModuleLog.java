package com.assessment.tracker.server.persistence.entities.logging;

import com.assessment.tracker.server.utils.enums.*;
import com.assessment.tracker.server.persistence.entities.Module;
import jakarta.persistence.*;

/**
 * Class for logging actions related to modules
 */
@Entity
public class ModuleLog extends Log {

    @ManyToOne
    @JoinColumn(name = "targetModule")
    private Module targetModule;

    private ModuleActions actionType;

    public Module getTargetModule() {
        return targetModule;
    }

    public void setTargetModule(Module targetModule) {
        this.targetModule = targetModule;
    }

    public ModuleActions getActionType() {
        return actionType;
    }

    public void setActionType(ModuleActions actionType) {
        this.actionType = actionType;
    }

}
