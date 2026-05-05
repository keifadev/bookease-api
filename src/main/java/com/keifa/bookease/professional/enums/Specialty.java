package com.keifa.bookease.professional.enums;
public enum Specialty {

    // Saúde & Bem-estar
    PSYCHOLOGIST("Psicólogo(a)", SpecialtyCategory.HEALTH),
    PSYCHIATRIST("Psiquiatra", SpecialtyCategory.HEALTH),
    DENTIST("Dentista", SpecialtyCategory.HEALTH),
    NUTRITIONIST("Nutricionista", SpecialtyCategory.HEALTH),
    PHYSIOTHERAPIST("Fisioterapeuta", SpecialtyCategory.HEALTH),
    SPEECH_THERAPIST("Fonoaudiólogo(a)", SpecialtyCategory.HEALTH),
    OCCUPATIONAL_THERAPIST("Terapeuta Ocupacional", SpecialtyCategory.HEALTH),
    ACUPUNCTURIST("Acupunturista", SpecialtyCategory.HEALTH),
    MASSAGE_THERAPIST("Massoterapeuta", SpecialtyCategory.HEALTH),

    // Beleza & Estética
    BARBER("Barbeiro", SpecialtyCategory.BEAUTY),
    HAIRDRESSER("Cabeleireiro(a)", SpecialtyCategory.BEAUTY),
    MANICURIST("Manicure / Pedicure", SpecialtyCategory.BEAUTY),
    ESTHETICIAN("Esteticista", SpecialtyCategory.BEAUTY),
    MAKEUP_ARTIST("Maquiador(a)", SpecialtyCategory.BEAUTY),
    EYEBROW_DESIGNER("Designer de Sobrancelhas", SpecialtyCategory.BEAUTY),
    TATTOO_ARTIST("Tatuador(a)", SpecialtyCategory.BEAUTY),
    NAIL_DESIGNER("Nail Designer", SpecialtyCategory.BEAUTY),
    DEPILATION_SPECIALIST("Especialista em Depilação", SpecialtyCategory.BEAUTY),

    // Fitness & Esporte
    PERSONAL_TRAINER("Personal Trainer", SpecialtyCategory.FITNESS),
    YOGA_INSTRUCTOR("Instrutor(a) de Yoga", SpecialtyCategory.FITNESS),
    PILATES_INSTRUCTOR("Instrutor(a) de Pilates", SpecialtyCategory.FITNESS),
    MARTIAL_ARTS_INSTRUCTOR("Instrutor(a) de Artes Marciais", SpecialtyCategory.FITNESS),
    SWIMMING_COACH("Professor(a) de Natação", SpecialtyCategory.FITNESS),
    DANCE_INSTRUCTOR("Professor(a) de Dança", SpecialtyCategory.FITNESS),

    // Educação & Consultoria
    PRIVATE_TUTOR("Professor(a) Particular", SpecialtyCategory.EDUCATION),
    MUSIC_TEACHER("Professor(a) de Música", SpecialtyCategory.EDUCATION),
    LANGUAGE_TEACHER("Professor(a) de Idiomas", SpecialtyCategory.EDUCATION),
    LIFE_COACH("Coach de Vida / Carreira", SpecialtyCategory.EDUCATION),
    FINANCIAL_ADVISOR("Consultor(a) Financeiro", SpecialtyCategory.EDUCATION),
    LAWYER("Advogado(a)", SpecialtyCategory.EDUCATION),

    // Pet Care
    VETERINARIAN("Veterinário(a)", SpecialtyCategory.PET_CARE),
    PET_GROOMER("Banho & Tosa", SpecialtyCategory.PET_CARE),
    DOG_TRAINER("Adestrador(a)", SpecialtyCategory.PET_CARE),
    PET_SITTER("Dog Walker / Pet Sitter", SpecialtyCategory.PET_CARE),

    // Tecnologia & Outros
    IT_CONSULTANT("Consultor(a) de TI", SpecialtyCategory.TECHNOLOGY),
    PHOTOGRAPHER("Fotógrafo(a)", SpecialtyCategory.TECHNOLOGY),
    OTHER("Outro", SpecialtyCategory.OTHER);

    private final String displayName;
    private final SpecialtyCategory category;

    Specialty(String displayName, SpecialtyCategory category) {
        this.displayName = displayName;
        this.category = category;
    }

    public String getDisplayName() {
        return displayName;
    }

    public SpecialtyCategory getCategory() {
        return category;
    }
}
