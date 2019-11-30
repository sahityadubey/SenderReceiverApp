using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;
using SenderReceiverApp.Models;
using SenderReceiverApps.Contracts;

namespace SenderReceiverApps.Controllers
{
    public class EmployeeController : Controller
    {
        private readonly ICosmosDbService _cosmosDbService;
        public EmployeeController(ICosmosDbService cosmosDbService)
        {
            _cosmosDbService = cosmosDbService;
        }

        // GET api/values
        [HttpGet]
        public async Task<ActionResult> Get(DateTime dateTime)
        {
            var getEmployees = await _cosmosDbService.GetAsync(dateTime);
            return Ok(getEmployees);
        }


        // POST api/values
        [HttpPost]
        public async Task<ActionResult> Post(EmployeeModel empoyeeModel)
        {
            await _cosmosDbService.PostAsync(empoyeeModel);
            return Ok();
        }
    }
}
