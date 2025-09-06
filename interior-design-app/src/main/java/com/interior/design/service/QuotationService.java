package com.interior.design.service;

import com.interior.design.domain.Project;
import com.interior.design.domain.Quotation;
import com.interior.design.domain.Role;
import com.interior.design.domain.User;
import com.interior.design.dto.QuotationDtos;
import com.interior.design.exception.ForbiddenOperationException;
import com.interior.design.exception.NotFoundException;
import com.interior.design.repository.ProjectRepository;
import com.interior.design.repository.QuotationRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class QuotationService {

    private final QuotationRepository quotationRepository;
    private final ProjectRepository projectRepository;
    private final CurrentUserService currentUserService;

    public QuotationService(QuotationRepository quotationRepository, ProjectRepository projectRepository, CurrentUserService currentUserService) {
        this.quotationRepository = quotationRepository;
        this.projectRepository = projectRepository;
        this.currentUserService = currentUserService;
    }

    public List<Quotation> getForProject(Long projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new NotFoundException("Project not found"));
        return quotationRepository.findByProject(project);
    }

    @Transactional
    public Quotation updateQuote(Long projectId, QuotationDtos.UpdateQuoteRequest req) {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new NotFoundException("Project not found"));
        User current = currentUserService.requireCurrentUser();
        if (current.getRole() == Role.VENDOR) {
            if (project.getVendor() == null || !project.getVendor().getId().equals(current.getId())) {
                throw new ForbiddenOperationException("You are not the vendor for this project");
            }
        } else if (current.getRole() != Role.ADMIN) {
            throw new ForbiddenOperationException("Not allowed");
        }
        Quotation q = new Quotation();
        q.setProject(project);
        if (req.materialsCost() != null) q.setMaterialsCost(req.materialsCost());
        if (req.laborCost() != null) q.setLaborCost(req.laborCost());
        if (req.customItemsCost() != null) q.setCustomItemsCost(req.customItemsCost());
        q.setStatus(Quotation.Status.SENT);
        return quotationRepository.save(q);
    }

    @Transactional
    public Quotation respond(Long quotationId, boolean accept) {
        Quotation q = quotationRepository.findById(quotationId).orElseThrow(() -> new NotFoundException("Quotation not found"));
        User current = currentUserService.requireCurrentUser();
        if (q.getProject().getCustomer() == null || !q.getProject().getCustomer().getId().equals(current.getId())) {
            throw new ForbiddenOperationException("Only the project customer can respond");
        }
        q.setStatus(accept ? Quotation.Status.ACCEPTED : Quotation.Status.DECLINED);
        return quotationRepository.save(q);
    }
}

