// id : unique ID for this environment
// dbname : alias for this environment
// prefix: internal prefix for this environment
// jdbc:${env.jdbcType}://${env.host}:${port}/${env.dbname}

dbconnections {
	cmn_test { 
		id = 1
		prefix = 'cmn_test'
		driverClassName = 'org.postgresql.Driver'
		jdbcType = 'postgresql'
		cinnamonServerUrl = 'http://172.16.13.130:8080/cinnamon/cinnamon'
		host = '172.16.13.130'
		port = 5432
		dbname = 'cmn_test'
		username = 'cinnamon'
		password = 'cinnamon'	
		encryptPasswords = 'true'
        dbUser = 'cinnamon'
		dbPassword = 'cinnamon'
    }

	demo { 
		id = 2
		prefix = 'demo'
		driverClassName = 'org.postgresql.Driver'
		jdbcType = 'postgresql'
		cinnamonServerUrl = 'http://172.16.13.130:8080/cinnamon/cinnamon'
		host = '172.16.13.130'
		port = 5432
		dbname = 'demo'
		username = 'cinnamon'
		password = 'cinnamon'
		encryptPasswords = 'true'
        dbUser = 'cinnamon'
		dbPassword = 'cinnamon'
        dbType = 'postgresql'
	}


}


indexers = [
            'server.index.indexer.DefaultIndexer',
            'server.index.indexer.BooleanXPathIndexer',
            'server.index.indexer.DateXPathIndexer',
            'server.index.indexer.IntegerXPathIndexer',
            'server.index.indexer.DefaultIndexer',
            'server.index.indexer.DecimalXPathIndexer',
            'server.index.indexer.TimeXPathIndexer',
            'server.index.indexer.ReverseStringIndexer',
            'server.index.indexer.ReverseCompleteStringIndexer',
            'server.index.indexer.ParentFolderPathIndexer',
            'server.index.indexer.DateTimeIndexer'
]

vaProviders = [
                       'server.index.valueAssistance.DefaultProvider',
]
