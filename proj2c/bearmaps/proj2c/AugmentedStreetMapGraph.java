package bearmaps.proj2c;

import bearmaps.hw4.WeightedEdge;
import bearmaps.hw4.streetmap.Node;
import bearmaps.hw4.streetmap.StreetMapGraph;
import bearmaps.proj2ab.Point;
import bearmaps.proj2ab.PointSet;
import bearmaps.proj2ab.WeirdPointSet;

import java.util.*;

/**
 * An augmented graph that is more powerful that a standard StreetMapGraph.
 * Specifically, it supports the following additional operations:
 *
 *
 * @author Alan Yao, Josh Hug, ________
 */
public class AugmentedStreetMapGraph extends StreetMapGraph {

    public AugmentedStreetMapGraph(String dbPath) {
        super(dbPath);
        // You might find it helpful to uncomment the line below:
        // List<Node> nodes = this.getNodes();
    }


    /**
     * For Project Part II
     * Returns the vertex closest to the given longitude and latitude.
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     */
    public long closest(double lon, double lat) {
        Map<Point, Node> pnMap = new HashMap<>();
        List<Point> pointList = new LinkedList<>();
        Set<Node> visited = new HashSet<>();
        for (Node node
                :
                getNodes()) {
            Point point = new Point(node.lon(), node.lat());
            if (!neighbors(node.id()).isEmpty()) {
                pnMap.put(point, node);
                pointList.add(point);
                visited.add(node);
            }
        }

        PointSet points = new WeirdPointSet(pointList);
        Point nearestPoint = points.nearest(lon, lat);

        return pnMap.get(nearestPoint).id();
    }


    /**
     * For Project Part III (gold points)
     * In linear time, collect all the names of OSM locations that prefix-match the query string.
     * @param prefix Prefix string to be searched for. Could be any case, with our without
     *               punctuation.
     * @return A <code>List</code> of the full names of locations whose cleaned name matches the
     * cleaned <code>prefix</code>.
     */
    public List<String> getLocationsByPrefix(String prefix) {
        return cleanedNameTrie.search(prefix);
    }
    private Trie cleanedNameTrie = new Trie();
    private class Trie {

        TrieNode root;
        HashMap<String, List<Node>> cleanedToNodes;

        private class TrieNode {
            boolean isAWord = false;
            boolean isDone = false;
            HashMap<Character,TrieNode> next = new HashMap<>();
            TrieNode() {}
        }

        Trie() {
            root = new TrieNode();
            cleanedToNodes = new HashMap<>();
            for (Node node
                    :
                    getNodes()) {
                if (node.name() != null) {
                    insert(node);
                }
            }
        }

        private LinkedList<String> search(String prefix) {
            LinkedList<String> nameList = new LinkedList<>();

            int preLen = prefix.length();
            StringBuilder sb = new StringBuilder();
            TrieNode cur = root;
            for (int i = 0; i < preLen; i++) {
                char c = prefix.charAt(i);
                if (cur.next.containsKey(c)) {
                    cur = cur.next.get(c);
                    sb.append(c);
                } else {
                    return nameList;
                }
            }

            searchHelper(nameList, cur, sb, preLen);
            return nameList;
        }

        private void searchHelper(LinkedList<String> names, TrieNode cur, StringBuilder sb, int backTrackLen) {
            if (cur.isDone) {
                for (Node node
                        :
                        cleanedToNodes.get(sb.toString())) {
                    names.add(node.name());
                }
            }

            for (Character nextChar
                    :
                    cur.next.keySet()) {
                sb.append(nextChar);
                searchHelper(names, cur.next.get(nextChar), sb, backTrackLen + 1);
                sb.deleteCharAt(backTrackLen);
            }
        }

        private void insert(Node node) {
            TrieNode cur = root;
            StringBuilder sb = new StringBuilder();
            String fullName = node.name();
            int len = fullName.length();
            for (int i = 0; i < len; i++) {
                char c = fullName.charAt(i);
                if (c >= 'A' && c <= 'Z') {
                    c += 32;
                }
                if (c == ' ') {
                    cur.isAWord = true;
                    continue;
                }

                if (!cur.next.containsKey(c)) {
                    cur.next.put(c, new TrieNode());
                }
                sb.append(c);
                cur = cur.next.get(c);
                if (i == len - 1) {
                    cur.isDone = true;
                }
            }
            String cleanedName = sb.toString();
            if (cleanedToNodes.containsKey(cleanedName)) {
                cleanedToNodes.get(cleanedName).add(node);
            } else {
                List<Node> newList = new LinkedList<>();
                newList.add(node);
                cleanedToNodes.put(cleanedName, newList);
            }
        }
    }

    /**
     * For Project Part III (gold points)
     * Collect all locations that match a cleaned <code>locationName</code>, and return
     * information about each node that matches.
     * @param locationName A full name of a location searched for.
     * @return A list of locations whose cleaned name matches the
     * cleaned <code>locationName</code>, and each location is a map of parameters for the Json
     * response as specified: <br>
     * "lat" -> Number, The latitude of the node. <br>
     * "lon" -> Number, The longitude of the node. <br>
     * "name" -> String, The actual name of the node. <br>
     * "id" -> Number, The id of the node. <br>
     */
    public List<Map<String, Object>> getLocations(String locationName) {
        List<Map<String, Object>> mapList = new LinkedList<>();

        StringBuilder getCleanedName = new StringBuilder();
        for (int i = 0; i < locationName.length(); i++) {
            char c = locationName.charAt(i);
            if (c == ' ') {
                continue;
            }
            if (c >= 'A' && c <= 'Z') {
                c += 32;
            }
            getCleanedName.append(c);
        }

        for (Node node
                :
                cleanedNameTrie.cleanedToNodes.get(getCleanedName.toString())) {
            Map<String, Object> map = new HashMap<>(){{
                put("lat", node.lat());
                put("lon", node.lon());
                put("name", node.name());
                put("id", node.id());
            }};
            mapList.add(map);
        }
        return mapList;
    }


    /**
     * Useful for Part III. Do not modify.
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    private static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

}
