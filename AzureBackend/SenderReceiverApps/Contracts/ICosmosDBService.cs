namespace SenderReceiverApps.Contracts
{
    using SenderReceiverApp.Models;
    using System;
    using System.Collections.Generic;
    using System.Threading.Tasks;

    public interface ICosmosDbService
    {
        Task PostAsync(EmployeeModel item);
        Task<IEnumerable<EmployeeModel>> GetAsync(DateTime item);
    }
}
