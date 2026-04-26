package tn.esprit.disasteraidbackend.modules.hours.service;

import tn.esprit.disasteraidbackend.modules.hours.dto.CertificateRequest;
import tn.esprit.disasteraidbackend.modules.hours.dto.CertificateResponse;

import java.util.List;

public interface CertificateService {

    CertificateResponse createCertificate(CertificateRequest request);

    CertificateResponse getCertificateById(Long id);

    List<CertificateResponse> getAllCertificates();

    List<CertificateResponse> getCertificatesByVolunteer(Long volunteerId);

    List<CertificateResponse> getCertificatesByOrg(Long orgId);

    CertificateResponse updateCertificate(Long id, CertificateRequest request);

    void deleteCertificate(Long id);
}
