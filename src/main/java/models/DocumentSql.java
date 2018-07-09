package models;

public class DocumentSql
{
    public static final String NAMED_SELECT_ALL = "Documents.SelectAll";
    public static final String SEARCH = "select * from dbo.Documents d where d.description %s :searchValue";
}
