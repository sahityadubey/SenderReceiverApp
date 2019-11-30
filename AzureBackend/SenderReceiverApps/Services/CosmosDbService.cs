namespace SenderReceiverApps.Services
{
    using System;
    using System.Collections.Generic;
    using System.Linq;
    using System.Threading.Tasks;
    using Microsoft.Azure.Cosmos;
    using SenderReceiverApp.Models;
    using SenderReceiverApps.Contracts;

    public class CosmosDbService : ICosmosDbService
    {
        private Container _container;

        public CosmosDbService(
            CosmosClient dbClient,
            string databaseName,
            string containerName)
        {
            _container = dbClient.GetContainer(databaseName, containerName);
        }
        public async Task<IEnumerable<EmployeeModel>> GetAsync(DateTime datetime)
        {
            var queryString = $"select * from c where c.LastModifiedTimeUTC > '{datetime.ToUniversalTime().ToString("o")}'";

            var query = this._container.GetItemQueryIterator<EmployeeModel>(new QueryDefinition(queryString));
            List<EmployeeModel> results = new List<EmployeeModel>();
            while (query.HasMoreResults)
            {
                var response = await query.ReadNextAsync();

                results.AddRange(response.ToList());
            }
            return results;
        }

        public async Task PostAsync(EmployeeModel item)
        {
            if (item != null)
            {
                var ifItemNotNullInDB = GetItemAsync(item.id);
                if (ifItemNotNullInDB == null)
                {
                    await _container.CreateItemAsync<EmployeeModel>(item, new PartitionKey(item.id));
                }
                else
                {
                    await UpdateItemAsync(item.id, item);
                }
            }
        }

        public async Task<EmployeeModel> GetItemAsync(string id)
        {
            try
            {
                ItemResponse<EmployeeModel> response = await _container.ReadItemAsync<EmployeeModel>(id, new PartitionKey(id));
                return response.Resource;
            }
            catch (CosmosException ex) when (ex.StatusCode == System.Net.HttpStatusCode.NotFound)
            {
                return null;
            }
        }

        public async Task UpdateItemAsync(string id, EmployeeModel item)
        {
            await _container.UpsertItemAsync<EmployeeModel>(item, new PartitionKey(id));
        }
    }
}
