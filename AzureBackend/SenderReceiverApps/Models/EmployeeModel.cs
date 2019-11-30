using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace SenderReceiverApp.Models
{
    public class EmployeeModel
    {
        [JsonProperty(PropertyName = "EmpDocId")]
        public string id { get; set; }
        public List<EmployeeModel> EmpDetails { get; set; }
        public bool isShouldSynced { get; set; }
        public string LastModifiedTimeUTC { get; set; }
    }
}