package com.gdb.models.users;

public class Verification {
    private VerificationType verficationType;
    private VerificationStatus verificationStatus;
    public VerificationType getVerficationType() {
        return verficationType;
    }
    public void setVerficationType(VerificationType verficationType) {
        this.verficationType = verficationType;
    }
    public VerificationStatus getVerificationStatus() {
        return verificationStatus;
    }
    public void setVerificationStatus(VerificationStatus verificationStatus) {
        this.verificationStatus = verificationStatus;
    }
}