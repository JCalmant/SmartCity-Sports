using System;

namespace Model
{
    public class Message
    {
        public int MessageId{ get; set;}
        public String CorpsMessage{get;set;}
        public DateTime Création{get;set;}

        public int AttributionGroupeId {get;set;}
        public virtual AttributionGroupe AttributionGroupe{get;set;}
    }
}