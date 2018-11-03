CREATE FUNCTION unaccent(regdictionary, text)
	RETURNS text
	AS '$libdir/unaccent', 'unaccent_dict'
	LANGUAGE C STABLE STRICT;

CREATE FUNCTION unaccent(text)
	RETURNS text
	AS '$libdir/unaccent', 'unaccent_dict'
	LANGUAGE C STABLE STRICT;

CREATE FUNCTION unaccent_init(internal)
	RETURNS internal
	AS '$libdir/unaccent', 'unaccent_init'
	LANGUAGE C;

CREATE FUNCTION unaccent_lexize(internal,internal,internal,internal)
	RETURNS internal
	AS '$libdir/unaccent', 'unaccent_lexize'
	LANGUAGE C;

CREATE TEXT SEARCH TEMPLATE unaccent (
	INIT = unaccent_init,
	LEXIZE = unaccent_lexize
);

CREATE TEXT SEARCH DICTIONARY unaccent (
	TEMPLATE = unaccent,
	RULES    = 'unaccent'
);
