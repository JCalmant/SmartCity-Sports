using Microsoft.EntityFrameworkCore;
using System;

namespace Model
{
    public class SportappsmartcitydbContext : Microsoft.AspNetCore.Identity.EntityFrameworkCore.IdentityDbContext<Utilisateur>
    {
        public SportappsmartcitydbContext(DbContextOptions options):base(options)
        {
            this.Database.EnsureCreated();
        }
        protected override void OnModelCreating(ModelBuilder modelBuilder){
            modelBuilder.HasDefaultSchema("smartcitycodefirst");
            base.OnModelCreating(modelBuilder);
                      
            modelBuilder.Entity<Sport>(entity =>
            {
                entity.ToTable("Sports", "smartcitycodefirst");
            });
            modelBuilder.Entity<Amitié>(entity =>
            {
                entity.ToTable("Amitiés", "smartcitycodefirst");

            
                entity.HasOne(ajouteur=>ajouteur.Ajouteur)
                    .WithMany(ajoutées=>ajoutées.AmitiésAjoutées)
                    .HasForeignKey(ajouteur=>ajouteur.AjouteurId)
                    .OnDelete(DeleteBehavior.ClientSetNull);

                entity.HasOne(ajouté=>ajouté.Ajouté)
                    .WithMany(ajouteur=>ajouteur.AmitiésAjouteur)
                    .HasForeignKey(ajouté=>ajouté.AjoutéId)
                    .OnDelete(DeleteBehavior.ClientSetNull);
            });
            modelBuilder.Entity<Disponibilite>(entity =>
            {
                entity.ToTable("Disponibilites", "smartcitycodefirst");
                entity.HasOne<Sport>(dispo => dispo.Sport)
                    .WithMany(sport => sport.Disponibilites)
                    .HasForeignKey(d => d.SportId)
                    .OnDelete(DeleteBehavior.ClientSetNull);

                entity.HasOne<ComplexeSportif>(dispo => dispo.ComplexeSportif)
                    .WithMany(complexe => complexe.Disponibilites)
                    .HasForeignKey(d => d.ComplexeSportifId)
                    .OnDelete(DeleteBehavior.ClientSetNull);

                entity.HasOne<Utilisateur>(dispo => dispo.Utilisateur)
                    .WithMany(dispo => dispo.Disponibilites)
                    .HasForeignKey(d => d.UtilisateurId)
                    .OnDelete(DeleteBehavior.ClientSetNull);
            });
            modelBuilder.Entity<ComplexeSportif>(entity =>
            {
                entity.ToTable("ComplexeSportifs", "smartcitycodefirst");
            });
            modelBuilder.Entity<AttributionGroupe>(entity =>
            {
                entity.ToTable("AttributionGroupes", "smartcitycodefirst");
            });
            modelBuilder.Entity<Groupe>(entity =>
            {
                entity.ToTable("Groupes", "smartcitycodefirst");
            });
            modelBuilder.Entity<HoraireBus>(entity =>
            {
                entity.ToTable("HorairesBus", "smartcitycodefirst");
            });
            modelBuilder.Entity<Message>(entity =>
            {
                entity.ToTable("Messages", "smartcitycodefirst");
            });
            modelBuilder.Entity<Utilisateur>(entity =>
            {
                entity.ToTable("AspNetUsers", "smartcitycodefirst");
            });
        }
       protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
        {
           
        }
        public DbSet<AttributionGroupe> AttributionGroupes { get; set; }
        public DbSet<ComplexeSportif> ComplexeSportifs { get; set; }
        public DbSet<Disponibilite> Disponibilites { get; set; }
        public DbSet<Groupe> Groupes { get; set; }
        public DbSet<HoraireBus> HorairesBus { get; set; }
        public DbSet<Message> Messages { get; set; }
        public DbSet<Sport> Sports { get; set; }
        public DbSet<Utilisateur> Utilisateurs { get; set; }
        public DbSet<Amitié> Amitiés{get;set;}
    }
}